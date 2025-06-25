package org.caesar.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.caesar.media.dto.LoginParam;
import org.caesar.media.entity.AdminUser;
import org.caesar.media.vo.LoginVo;

import javax.security.sasl.AuthenticationException;

/**
 * @Description: 后台用户Service接口
 * @Author: peng.guo
 * @Create: 2025-05-27 15:50
 * @Version 1.0
 **/
public interface AdminUserService extends IService<AdminUser> {

    /**
     * 登录
     * @param loginParam 登录参数
     * @return token
     */
    LoginVo login(LoginParam loginParam) throws AuthenticationException;
}
