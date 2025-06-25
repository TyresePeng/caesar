package org.caesar.media.vo;

import lombok.Data;


/**
 * @Description: 后台用户实体类
 * @Author: peng.guo
 * @Create: 2025-05-27 15:50
 * @Version 1.0
 **/
@Data
public class AdminUserVo {

    /**
     * 主键ID
     */
    private Long userId=0L;

    /**
     * 用户名
     */
    private String username="admin";

    /**
     * 头像
     */
    private String avatar="https://avatars.githubusercontent.com/u/49427374?v=4";

    private String nickname="admin";

    private String[] perms=new String[]{"*"};
    private String[] roles=new String[]{"ROOT"};
}
