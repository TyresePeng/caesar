-- 设置连接编码
SET NAMES utf8mb4;

CREATE TABLE `caesar_admin_user` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `username` varchar(64) DEFAULT NULL,
                                     `password` varchar(64) DEFAULT NULL,
                                     `icon` varchar(500) DEFAULT NULL COMMENT '头像',
                                     `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
                                     `nick_name` varchar(200) DEFAULT NULL COMMENT '昵称',
                                     `note` varchar(500) DEFAULT NULL COMMENT '备注信息',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                                     `status` int(1) DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用',
                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='后台用户表';

INSERT INTO `caesar_crawler`.`caesar_admin_user` (`id`, `username`, `password`, `icon`, `email`, `nick_name`, `note`, `create_time`, `login_time`, `status`) VALUES (1, 'admin', '123456', NULL, NULL, NULL, NULL, NULL, NULL, 1);

CREATE TABLE `caesar_job_config` (
                                     `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                     `title` varchar(255) NOT NULL COMMENT '任务标题',
                                     `platform_code` varchar(64) NOT NULL COMMENT '平台编码，如：douyin、kuaishou',
                                     `job_type` varchar(64) NOT NULL DEFAULT 'default' COMMENT '任务类型，如：fetch_video、generate_script、transcode_video',
                                     `params` text COMMENT '执行参数，JSON格式',
                                     `cron` varchar(128) DEFAULT NULL COMMENT '定时任务表达式',
                                     `status` tinyint(1) DEFAULT '1' COMMENT '是否启用，0：禁用，1：启用',
                                     `remark` varchar(512) DEFAULT NULL COMMENT '备注信息',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_platform_code` (`platform_code`),
                                     KEY `idx_job_type` (`job_type`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='任务表';


CREATE TABLE `caesar_job_resource` (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                       `job_config_id` bigint(20) NOT NULL COMMENT '关联的任务配置ID',
                                       `platform_code` varchar(50) NOT NULL COMMENT '平台编码，如：douyin、kuaishou',
                                       `resource_type` varchar(50) NOT NULL COMMENT '资源类型，如：video、image、audio、text',
                                       `resource_id` text COMMENT '资源id',
                                       `title` longtext COMMENT '资源标题',
                                       `url` longtext COMMENT '资源链接地址',
                                       `cover_url` longtext COMMENT '封面链接（视频、图集等）',
                                       `author` longtext COMMENT '资源作者',
                                       `tags` longtext COMMENT '标签，逗号分隔',
                                       `meta` longtext COMMENT '元信息，JSON 格式',
                                       `downloaded` tinyint(1) DEFAULT '0' COMMENT '是否已下载，0：未下载，1：已下载',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2290 DEFAULT CHARSET=utf8mb4 COMMENT='任务爬取资源表';


CREATE TABLE `caesar_platform_user` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                        `name` varchar(300) DEFAULT NULL COMMENT '帐号名称',
                                        `code` varchar(50) DEFAULT NULL COMMENT '平台code',
                                        `session_storage` longtext COMMENT '会话存储',
                                        `status` tinyint(1) DEFAULT '0' COMMENT '状态：1有效，2失效',
                                        `sort` int(4) DEFAULT '0' COMMENT '排序',
                                        `check_session_msg` longtext COMMENT '检查会话信息',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='平台帐号表';


CREATE TABLE `caesar_douyin_live_messages` (
                                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                               `msg_id` bigint(20) NOT NULL COMMENT '抖音消息ID或唯一标识（用于去重）',
                                               `room_id` varchar(50) DEFAULT NULL COMMENT '直播间ID',
                                               `method` varchar(100) DEFAULT NULL COMMENT '消息类型/事件类型，例如 chatMessage、giftMessage',
                                               `content` text COMMENT '简化文本内容（例如弹幕内容、礼物名等）',
                                               `payload` blob COMMENT '原始消息 JSON 数据（二进制或字符串存储）',
                                               `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
                                               `processed` tinyint(1) DEFAULT '0' COMMENT '是否已处理（用于轮询或后台任务标记）',
                                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23179 DEFAULT CHARSET=utf8mb4 COMMENT='抖音直播通用消息存储表';
