package org.caesar.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.caesar.media.entity.DouyinLiveMessage;

public interface DouyinLiveMessageService extends IService<DouyinLiveMessage> {

    /**
     * 保存直播消息至数据库
     *
     * @param msgId   消息唯一 ID
     * @param roomId  直播间 ID
     * @param method  消息类型
     * @param content 消息内容
     * @param payload 原始消息二进制数据
     */
    void saveMessage(long msgId, String roomId, String method, String content, byte[] payload);
}
