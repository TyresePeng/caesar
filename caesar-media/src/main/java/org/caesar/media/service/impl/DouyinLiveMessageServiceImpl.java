package org.caesar.media.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.caesar.media.entity.DouyinLiveMessage;
import org.caesar.media.mapper.DouyinLiveMessageMapper;
import org.caesar.media.service.DouyinLiveMessageService;
import org.springframework.stereotype.Service;

@Service
public class DouyinLiveMessageServiceImpl extends ServiceImpl<DouyinLiveMessageMapper, DouyinLiveMessage>implements DouyinLiveMessageService {

    /**
     * 保存直播消息至数据库
     *
     * @param msgId   消息唯一 ID
     * @param roomId  直播间 ID
     * @param method  消息类型
     * @param content 消息内容
     * @param payload 原始消息二进制数据
     */
    public void saveMessage(long msgId, String roomId, String method, String content, byte[] payload) {
        DouyinLiveMessage message = new DouyinLiveMessage();
        message.setMsgId(msgId);
        message.setRoomId(roomId);
        message.setMethod(method);
        message.setContent(content);
        message.setPayload(payload);
        message.setProcessed(false);
        this.save(message);
    }
}
