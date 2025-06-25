package org.caesar.media.controller.douyin;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.caesar.boot.start.exception.BusinessException;
import org.caesar.common.response.ApiResponse;
import org.caesar.media.client.DouyinClient;
import org.caesar.media.client.DouyinLiveClient;
import org.caesar.media.enums.PublishTimeType;
import org.caesar.media.enums.SearchChannelType;
import org.caesar.media.enums.SearchSortType;
import org.caesar.media.service.LiveRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * @Description: 抖音控制器
 * @Author: peng.guo
 * @Create: 2025-05-27 15:50
 * @Version 1.0
 **/
@RestController
@RequestMapping("/douyin")
@RequiredArgsConstructor
@Slf4j
public class DouyinController {
    private final DouyinClient douyinClient;
    private final DouyinLiveClient douyinLiveClient;
    private final LiveRecordService liveRecordService;

    /**
     * 查询关键词
     *
     * @param keyword           关键词
     * @param offset            偏移量
     * @param count             数量
     * @param publishTimeType   发布时间类型
     * @param searchChannelType 搜索频道类型
     * @param searchSortType    搜索排序类型
     * @return 查询结果
     */
    @GetMapping("query-key-word")
    public ApiResponse<JSONObject> queryKeyWord(String keyword,
                                                int offset,
                                                int count,
                                                PublishTimeType publishTimeType,
                                                SearchChannelType searchChannelType,
                                                SearchSortType searchSortType) {
        return ApiResponse.success(douyinClient.queryKeyWord(keyword,
                offset,
                count,
                publishTimeType,
                searchChannelType,
                searchSortType

        ));
    }

    /**
     * 根据直播间id查询直播间信息
     *
     * @param roomId 房间号
     * @return 查询结果
     */
    @GetMapping("query-room")
    public ApiResponse<JSONObject> queryRoom(String roomId) {
        return ApiResponse.success(douyinLiveClient.queryRoom(roomId));
    }

    /**
     * 链接直播间
     *
     * @param roomId 房间号
     */
    @GetMapping("connect-room")
    public ApiResponse<Void> connectRoom(String roomId) {
        douyinLiveClient.connectRoom(roomId);
        return ApiResponse.success();
    }

    /**
     * 断开直播间
     *
     * @param roomId 房间号
     */
    @GetMapping("disconnect-room")
    public ApiResponse<Void> disconnectRoom(String roomId) {
        douyinLiveClient.disconnectRoom(roomId);
        return ApiResponse.success();
    }

    /**
     * 直播录制
     *
     * @param roomId 房间号
     */
    @GetMapping("live-record")
    public ApiResponse<Void> liveRecord(String roomId) {
        try {
            liveRecordService.startRecording(roomId);
        } catch (Exception e) {
            return ApiResponse.fail(e.getMessage());
        }
        return ApiResponse.success();
    }

    /**
     * 停止直播录制
     *
     * @param roomId 房间号
     */
    @GetMapping("stop-live-record")
    public ApiResponse<Void> stopLiveRecord(String roomId) {
        liveRecordService.stopRecording(roomId);
        return ApiResponse.success();
    }

    /**
     * 获取直播录制状态
     *
     * @param roomId 房间号
     */
    @GetMapping("live-record-status")
    public ApiResponse<Boolean> liveRecordStatus(String roomId) {
        return ApiResponse.success(liveRecordService.isRecording(roomId));
    }


    /**
     * 直播录制文件下载接口
     *
     * @param roomId   直播间ID
     * @param response HttpServletResponse
     */
    @GetMapping("/record/download/{roomId}")
    public void downloadRecording(@PathVariable String roomId, HttpServletResponse response) {
        try {
            liveRecordService.downloadRecordingToResponse(roomId, response);
        } catch (Exception e) {
            log.error("服务器内部错误:{}", e.getMessage(), e);
        }
    }

}
