package org.caesar.media.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务资源实体类，对应表 caesar_job_resource
 */
@Data
@TableName("caesar_job_resource")
public class JobResource {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务配置ID（外键）
     */
    private Long jobConfigId;

    /**
     * 平台编码
     */
    private String platformCode;

    /**
     * 资源ID
     */
    private String resourceId;



    /**
     * 资源类型：video/image/audio/text
     */
    private String resourceType;

    /**
     * 资源标题
     */
    private String title;

    /**
     * 资源链接
     */
    private String url;

    /**
     * 封面链接
     */
    private String coverUrl;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签，多个用英文逗号隔开
     */
    private String tags;

    /**
     * 元信息，JSON 格式
     */
    private String meta;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
