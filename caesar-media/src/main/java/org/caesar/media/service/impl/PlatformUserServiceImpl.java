package org.caesar.media.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.caesar.media.entity.PlatformUser;
import org.caesar.media.mapper.PlatformUserMapper;
import org.caesar.media.service.PlatformUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 平台用户 Service 实现类
 * 继承 ServiceImpl 提供 IService 默认实现
 */
@Service
@AllArgsConstructor
public class PlatformUserServiceImpl extends ServiceImpl<PlatformUserMapper, PlatformUser> implements PlatformUserService {


    @Override
    public List<PlatformUser> platformActiveUserList() {
        return this.list(Wrappers.<PlatformUser>lambdaQuery().eq(PlatformUser::getStatus, 1));
    }

    @Override
    public void updateStatus(Integer status, Long id) {
        PlatformUser platformUser = new PlatformUser();
        platformUser.setId(id);
        platformUser.setStatus(status);
        this.updateById(platformUser);
    }

}
