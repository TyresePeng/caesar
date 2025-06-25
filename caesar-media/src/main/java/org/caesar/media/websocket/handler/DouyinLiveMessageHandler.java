package org.caesar.media.websocket.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import org.caesar.media.proto.douyin.Live;

/**
 * 抖音消息处理接口，用户实现此接口以自定义消息处理逻辑
 */
public interface DouyinLiveMessageHandler {

    /**
     * 接收并处理一条抖音直播消息
     * @param msg 抖音服务器推送的消息
     */
    void onMessage(Live.Message msg) throws InvalidProtocolBufferException;

    /**
     * （可选）WebSocket 关闭回调
     */
    default void onClose(int code, String reason) {}

    /**
     * （可选）连接或处理异常时回调
     */
    default void onError(Throwable t) {}
}
