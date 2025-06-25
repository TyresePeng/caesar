package org.caesar.media.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务配置实体类，映射数据库表 caesar_job_config
 */
@Data
@TableName("caesar_job_config")
public class JobConfig {

    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 平台编码，如：douyin、kuaishou
     */
    private String platformCode;

    /**
     * 任务类型，如：fetch_video、generate_script、transcode_video
     */
    private String jobType;

    /**
     * 执行参数，JSON格式字符串
     */
    private String params;


    /**
     * 定时任务表达式
     */
    private String cron;

    /**
     * 是否启用，0：禁用，1：启用 ,3 执行完成
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间，自动填充
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间，自动填充
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
