package org.caesar.media.service;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.caesar.boot.start.exception.BusinessException;
import org.caesar.media.client.DouyinLiveClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 抖音直播录制服务（使用 Java 进程方式执行 FFmpeg）
 * 支持：
 * - 拉流录制
 * - 优雅中断
 * - 断点下载
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LiveRecordService {

    @Value("${live.record.path}")
    private String liveRecordPath;

    /** 正在录制的任务缓存 */
    private final Map<String, RecordTask> taskMap = new ConcurrentHashMap<>();

    /** 已完成或中断的录制记录（供下载使用） */
    private final Map<String, CopyOnWriteArrayList<Path>> recordedFilesMap = new ConcurrentHashMap<>();

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final DouyinLiveClient douyinLiveClient;

    /**
     * 启动录制任务
     */
    public void startRecording(String roomId) {
        if (taskMap.containsKey(roomId)) {
            throw new IllegalStateException("该直播间正在录制中");
        }

        // 获取拉流地址
        JSONObject roomJson = douyinLiveClient.queryRoom(roomId);
        if (roomJson == null) {
            throw new BusinessException("查询直播间信息失败");
        }

        JSONObject streamInfo = roomJson.getJSONObject("data")
                .getJSONArray("data")
                .getJSONObject(0)
                .getJSONObject("stream_url");
        String streamUrl = streamInfo.getString("hls_pull_url");

        if (streamUrl == null || streamUrl.isEmpty()) {
            throw new BusinessException("直播间无有效拉流地址");
        }

        Path outputPath = Paths.get(liveRecordPath, roomId + "_" + System.currentTimeMillis() + ".mp4");

        executor.submit(() -> {
            Process process = null;
            try {
                // 构建 FFmpeg 命令（无后台 &）
                String[] command = {
                        "ffmpeg", "-y",
                        "-i", streamUrl,
                        "-c:v", "libx264", "-preset", "veryfast",
                        "-c:a", "aac",
                        "-movflags", "+faststart",
                        "-f", "mp4",
                        outputPath.toString()
                };

                log.info("启动录制 FFmpeg：{}", String.join(" ", command));

                process = new ProcessBuilder(command)
                        .redirectErrorStream(true)
                        .start();

                // 保存任务引用
                taskMap.put(roomId, new RecordTask(roomId, streamUrl, outputPath, process, System.currentTimeMillis()));

                // 阻塞等待录制完成（或手动中断）
                int exitCode = process.waitFor();
                log.info("录制结束：roomId={}, path={}, exitCode={}", roomId, outputPath, exitCode);
            } catch (Exception e) {
                log.error("录制过程中异常：roomId=" + roomId, e);
            } finally {
                // 即使录制失败也记录文件供下载
                recordedFilesMap.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(outputPath);
                taskMap.remove(roomId);
            }
        });
    }

    /**
     * 停止录制（优雅方式）
     */
    public void stopRecording(String roomId) {
        RecordTask task = taskMap.get(roomId);
        if (task != null && task.getProcess().isAlive()) {
            try {
                stopFfmpeg(task.getProcess());
                log.info("已发送 destroy() 停止录制：roomId={}", roomId);
            } catch (Exception e) {
                log.warn("停止录制失败：roomId={}", roomId, e);
            }

            // 记录中断文件
            recordedFilesMap.computeIfAbsent(roomId, k -> new CopyOnWriteArrayList<>()).add(task.getOutputPath());
            taskMap.remove(roomId);
        }
    }

    /**
     * 停止 FFmpeg
     * @param ffmpegProcess Process
     */
    public void stopFfmpeg(Process ffmpegProcess) {
        try (OutputStream os = ffmpegProcess.getOutputStream()) {
            // 通知 FFmpeg 正常退出
            os.write('q');
            os.flush();
            ffmpegProcess.waitFor(); // 等待退出完成
        } catch (IOException | InterruptedException e) {
            log.error("停止 FFmpeg 失败:{}", e.getMessage(),e);
            ffmpegProcess.destroy(); // 兜底
        }
    }
    /**
     * 判断是否正在录制
     */
    public boolean isRecording(String roomId) {
        return taskMap.containsKey(roomId) && taskMap.get(roomId).getProcess().isAlive();
    }

    /**
     * 获取最近一次录制文件（不管是否录制中断）
     */
    public Path getRecordedPath(String roomId) {
        RecordTask task = taskMap.get(roomId);
        if (task != null && Files.exists(task.getOutputPath())) {
            return task.getOutputPath();
        }

        List<Path> list = recordedFilesMap.get(roomId);
        if (list != null && !list.isEmpty()) {
            Path last = list.get(list.size() - 1);
            if (Files.exists(last)) return last;
        }

        throw new BusinessException("未找到录制文件");
    }

    /**
     * 下载录制文件到响应体
     */
    public void downloadRecordingToResponse(String roomId, HttpServletResponse resp) {
        Path path = getRecordedPath(roomId);
        if (!Files.exists(path)) {
            throw new BusinessException("录制文件不存在");
        }

        // 设置响应头
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + path.getFileName() + "\"");
        resp.setContentLengthLong(path.toFile().length());

        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(path));
             OutputStream os = resp.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            log.error("下载录制文件失败：roomId=" + roomId, e);
            throw new BusinessException("下载失败");
        }
    }

    /**
     * 应用关闭时清理资源
     */
    @PreDestroy
    public void shutdown() {
        log.info("应用关闭，停止所有录制任务...");
        for (String roomId : taskMap.keySet()) {
            stopRecording(roomId);
        }
        executor.shutdown();
    }

    /**
     * 录制任务对象
     */
    @Data
    @RequiredArgsConstructor
    private static class RecordTask {
        private final String roomId;
        private final String streamUrl;
        private final Path outputPath;
        private final Process process;
        private final long startTime;
    }
}
