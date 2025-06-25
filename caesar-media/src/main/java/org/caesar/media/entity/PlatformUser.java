package org.caesar.media.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 平台用户实体类，对应数据库表 caesar_platform_user
 * @author peng.guo
 */
@TableName("caesar_platform_user")
@Data
public class PlatformUser {

    /**
     * 主键ID，自增
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 帐号名称
     */
    private String name;

    /**
     * 平台编码，如抖音、微信等
     */
    private String code;

    /**
     * 会话存储，保存用户会话信息
     */
    @TableField("session_storage")
    private String sessionStorage;


    /**
     * 会话存储，保存用户会话信息
     */
    @TableField("check_session_msg")
    private String checkSessionMsg;



    /**
     * 状态，1-有效，2-失效
     */
    private Integer status;

    /**
     * 排序字段，用于前端显示排序
     */
    private Integer sort;

    /**
     * 创建时间，自动填充
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间，自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
