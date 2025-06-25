package org.caesar.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.caesar.media.dto.LoginParam;
import org.caesar.media.entity.AdminUser;
import org.caesar.media.mapper.AdminUserMapper;
import org.caesar.media.service.AdminUserService;
import org.caesar.media.vo.LoginVo;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

/**
 * @Description: 后台用户Service实现类
 * @Author: peng.guo
 * @Create: 2025-05-27 15:50
 * @Version 1.0
 **/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {
    @Override
    public LoginVo login(LoginParam loginParam) throws AuthenticationException {
        AdminUser adminUser = this.getOne(
                Wrappers.<AdminUser>lambdaQuery().
                        eq(AdminUser::getUsername, loginParam.getUsername())
                        .eq(AdminUser::getPassword, loginParam.getPassword())
        );
        if (adminUser != null) {
            return new LoginVo();
        }
        throw new AuthenticationException("用户不存在");
    }
}
