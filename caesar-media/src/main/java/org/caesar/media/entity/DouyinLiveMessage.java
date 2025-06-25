package org.caesar.media.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 抖音直播消息实体类
 * 对应数据库表：caesar_douyin_live_messages
 */
@Data
@TableName("caesar_douyin_live_messages")
public class DouyinLiveMessage {

    /**
     * 自增主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 抖音消息ID，直播间内唯一标识
     */
    private Long msgId;

    /**
     * 直播间ID，用于区分不同直播间的消息
     */
    private String roomId;

    /**
     * 消息类型，如 WebcastGiftMessage、WebcastChatMessage 等
     */
    private String method;

    /**
     * 消息简要内容，如弹幕文本、礼物名称等
     */
    private String content;

    /**
     * 消息的原始Payload二进制数据，存储为BLOB类型
     */
    private byte[] payload;

    /**
     * 是否已处理标记，方便后续逻辑区分
     */
    private Boolean processed;

    /**
     * 创建时间，自动填充插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
