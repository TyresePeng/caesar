package org.caesar.media.controller.admin;

import lombok.RequiredArgsConstructor;
import org.caesar.common.response.ApiResponse;
import org.caesar.media.dto.LoginParam;
import org.caesar.media.service.AdminUserService;
import org.caesar.media.vo.AdminUserVo;
import org.caesar.media.vo.LoginVo;
import org.caesar.media.vo.MenuVo;
import org.caesar.media.vo.RouteVo;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.List;

/**
 * TODO 预留口子，可扩展开发
 * @Description: 后台权限相关
 * @Author: peng.guo
 * @Create: 2025-05-30 15:39
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public ApiResponse<LoginVo> login(LoginParam loginParam) throws AuthenticationException {
        return ApiResponse.success(adminUserService.login(loginParam));
    }

    @DeleteMapping("/logout")
    public ApiResponse<Boolean> logout() {
        return ApiResponse.success(true);
    }

    @GetMapping("/user-info")
    public ApiResponse<AdminUserVo> userInfo() {
        return ApiResponse.success(new AdminUserVo());
    }

    @GetMapping("/menus/routes")
    public ApiResponse<List<MenuVo>> routes() {
        return ApiResponse.success(RouteVo.getMenuList());
    }

}
