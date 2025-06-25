package org.caesar.media.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.caesar.media.entity.PlatformUser;

import java.util.List;

/**
 * 平台用户 Service 接口
 * 继承 MyBatis-Plus IService 接口，封装基本业务操作
 */
public interface PlatformUserService extends IService<PlatformUser> {

     List<PlatformUser> platformActiveUserList();

     void updateStatus(Integer status,Long id);
}
