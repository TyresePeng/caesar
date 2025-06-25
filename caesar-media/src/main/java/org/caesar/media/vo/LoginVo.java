package org.caesar.media.vo;

import lombok.Data;

/**
 * @Description: 登录返回
 * @Author: peng.guo
 * @Create: 2025-05-30 18:22
 * @Version 1.0
 **/
@Data
public class LoginVo {
    private String accessToken="eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImp0aSI6IjE2YWJkNTlkOTAxNzQwZDliYmI3ZjczODBhZDkyNzNhIiwidXNlcklkIjoyLCJ1c2VybmFtZSI6ImFkbWluIiwiZGVwdElkIjoxLCJkYXRhU2NvcGUiOjEsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iXSwiZXhwIjoxNjkxMTAzMzgyfQ.P4cuIfmPepl3HuguhMS7NXn5a7IUPpsLbmtA_rHOhHk";
    private String expires;
    private String refreshToken;
    private String tokenType="Bearer";
}
