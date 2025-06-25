package org.caesar.media.websocket;

import okhttp3.*;
import okio.ByteString;
import org.caesar.media.proto.douyin.Live;
import org.caesar.media.websocket.handler.DouyinLiveMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

/**
 * 抖音直播 WebSocket 客户端，支持自定义消息处理器
 * 连接后自动发送心跳，接收消息并解压分发
 * @author peng.guo
 */
public class DouyinLiveWebSocket extends WebSocketListener {

    private static final Logger log = LoggerFactory.getLogger(DouyinLiveWebSocket.class);

    private final OkHttpClient client;
    private final String wsUrl;
    private WebSocket ws;
    private Timer heartbeatTimer;
    private final DouyinLiveMessageHandler messageHandler;

    /**
     * 构造方法
     * @param wsUrl WebSocket 地址
     * @param messageHandler 自定义消息处理器
     */
    public DouyinLiveWebSocket(String wsUrl, DouyinLiveMessageHandler messageHandler) {
        this.wsUrl = wsUrl;
        this.messageHandler = messageHandler;
        this.client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 建立 WebSocket 连接
     * @param headers 连接请求头，需包含必要的 Cookie
     */
    public void connect(Headers headers) {
        Request request = new Request.Builder()
                .url(wsUrl)
                .headers(headers)
                .build();
        this.ws = client.newWebSocket(request, this);
    }

    /**
     * 主动关闭连接
     */
    public void disconnect() {
        if (ws != null) {
            ws.close(1000, "User disconnect");
        }
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info("WebSocket 已连接: {}", wsUrl);
        heartbeatTimer = new Timer(true);
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Live.PushFrame heartbeat = Live.PushFrame.newBuilder()
                        .setPayloadType("hb")
                        .build();
                ws.send(ByteString.of(heartbeat.toByteArray()));
                log.debug("心跳包已发送");
            }
        }, 0, 5000);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        try {
            Live.PushFrame frame = Live.PushFrame.parseFrom(bytes.toByteArray());
            byte[] decompressed = decompress(frame.getPayload().toByteArray());
            Live.LiveResponse response = Live.LiveResponse.parseFrom(decompressed);

            if (response.getNeedAck()) {
                Live.PushFrame ack = Live.PushFrame.newBuilder()
                        .setPayloadType("ack")
                        .setPayload(com.google.protobuf.ByteString.copyFromUtf8(response.getInternalExt()))
                        .setLogId(frame.getLogId())
                        .build();
                ws.send(ByteString.of(ack.toByteArray()));
            }

            List<Live.Message> messages = response.getMessagesListList();
            for (Live.Message msg : messages) {
                messageHandler.onMessage(msg);
            }

        } catch (Exception e) {
            log.error("WebSocket 消息解析异常", e);
            messageHandler.onError(e);
        }
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        log.warn("WebSocket 正在关闭，code={}，reason={}", code, reason);
        if (heartbeatTimer != null) {
            heartbeatTimer.cancel();
        }
        messageHandler.onClose(code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        log.warn("WebSocket 已关闭，code={}，reason={}", code, reason);
        if (heartbeatTimer != null) {
            heartbeatTimer.cancel();
        }
        messageHandler.onClose(code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        log.error("WebSocket 连接失败", t);
        messageHandler.onError(t);
    }

    /**
     * 使用 GZIP 解压字节数组
     * @param data GZIP 压缩数据
     * @return 解压后的字节数组
     * @throws IOException 发生IO异常时抛出
     */
    private byte[] decompress(byte[] data) throws IOException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             GZIPInputStream gzip = new GZIPInputStream(bais);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int len;
            while ((len = gzip.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }
}
