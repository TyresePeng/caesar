package org.caesar.media.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.caesar.common.response.ApiResponse;
import org.caesar.media.client.DouyinClient;
import org.caesar.media.dto.PlatformUserDto;
import org.caesar.media.dto.PlatformUserQuery;
import org.caesar.media.entity.PlatformUser;
import org.caesar.media.factory.PlaywrightFactoryPool;
import org.caesar.media.service.PlatformUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @Description: 平台管理
 * @Author: peng.guo
 * @Create: 2025-06-03 16:18
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/platform")
@RequiredArgsConstructor
public class PlatformController {

    private final PlatformUserService platformUserService;
    private final PlaywrightFactoryPool playwrightFactoryPool;

    /**
     * @Description: 保存平台用户
     * @Param: [param]
     * @return: org.caesar.common.response.ApiResponse<java.lang.Boolean>
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     **/
    @PostMapping("save-platform-user")
    public ApiResponse<Boolean> savePlatformUser(@RequestBody PlatformUserDto param) {
        PlatformUser platformUser = new PlatformUser();
        platformUser.setName(param.getName());
        platformUser.setCode(param.getCode());
        platformUser.setSort(param.getSort());
        platformUser.setCreateTime(LocalDateTime.now());
        platformUser.setUpdateTime(LocalDateTime.now());
        platformUserService.save(platformUser);
        return ApiResponse.success(true);
    }

    /**
     * @Description: 删除平台用户
     * @Param: [id]
     * @return: org.caesar.common.response.ApiResponse<java.lang.Boolean>
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     **/
    @DeleteMapping("delete-platform-user")
    public ApiResponse<Boolean> deletePlatformUser(String  idList) {
        if (idList != null ) {
            String[] split = idList.split(",");
            return ApiResponse.success(platformUserService.removeBatchByIds(Arrays.asList(split)));
        }
        return ApiResponse.success(true);
    }


    /**
     * @Description: 删除平台用户
     * @Param: [id]
     * @return: org.caesar.common.response.ApiResponse<java.lang.Boolean>
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     **/
    @DeleteMapping("delete-platform-user/{id}")
    public ApiResponse<Boolean> deletePlatformUser(@PathVariable Long id) {
        boolean flag = platformUserService.removeById(id);
        if (flag){
            //todo 停掉所有工厂实例 可优化
            playwrightFactoryPool.shutdown();
            playwrightFactoryPool.initPool();
        }
        return ApiResponse.success(platformUserService.removeById(id));
    }

    /**
     * @Description: 更新平台用户
     * @Param: [param]
     * @return: org.caesar.common.response.ApiResponse<java.lang.Boolean>
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     **/
    @PutMapping("update-platform-user/{id}")
    public ApiResponse<Boolean> updatePlatformUser(@PathVariable Long id,@RequestBody PlatformUserDto param) {
        PlatformUser platformUser = new PlatformUser();
        platformUser.setId(id);
        platformUser.setName(param.getName());
        platformUser.setCode(param.getCode());
        platformUser.setSort(param.getSort());
        platformUser.setUpdateTime(LocalDateTime.now());
        return ApiResponse.success(platformUserService.updateById(platformUser));
    }

    /**
     * @Description: 获取平台用户
     * @Param: [id]
     * @return: org.caesar.common.response.ApiResponse<org.caesar.crawler.entity.PlatformUser>
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     **/
    @GetMapping("get-platform-user/{id}")
    public ApiResponse<PlatformUser> getPlatformUser(@PathVariable Long id) {
        return ApiResponse.success(platformUserService.getById(id));
    }

    /**
     * @Description: 获取平台用户分页列表
     * @Param: query 查询条件，包含分页信息和过滤字段
     * @return: ApiResponse 分页结果
     * @Author: peng.guo
     * @Create: 2025/6/3 16:19
     */
    @PostMapping("/list-platform-user-page")
    public ApiResponse<IPage<PlatformUser>> listPlatformUserPage(@RequestBody PlatformUserQuery query) {
        // 构建分页对象
        Page<PlatformUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        // 构建查询条件
        LambdaQueryWrapper<PlatformUser> wrapper = new LambdaQueryWrapper<>();
        if (query.getKeywords() != null && !query.getKeywords().isEmpty()) {
            wrapper.like(PlatformUser::getName, query.getKeywords());
        }
        // 分页查询
        IPage<PlatformUser> resultPage = platformUserService.page(page, wrapper);
        return ApiResponse.success(resultPage);
    }

    /**
     * 上传storage文件
     */
    @PostMapping("/upload/storage/file/{id}")
    public ApiResponse<Boolean> uploadStorageFile(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws IOException {
        PlatformUser platformUser = new PlatformUser();
        platformUser.setId(id);
        if (!multipartFile.isEmpty()){
            String fileContent = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
            platformUser.setSessionStorage(fileContent);
            platformUser.setUpdateTime(LocalDateTime.now());
            platformUser.setStatus(1);
            boolean flag = platformUserService.updateById(platformUser);
            if (flag){
                //todo 停掉所有工厂实例 可优化
                playwrightFactoryPool.shutdown();
                playwrightFactoryPool.initPool();
            }
            return ApiResponse.success(flag);
        }
        return ApiResponse.success(false);
    }

}
