package org.caesar.media.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.caesar.common.response.ApiResponse;
import org.caesar.media.entity.JobConfig;
import org.caesar.media.service.JobConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: 任务控制器
 * @Author: peng.guo
 * @Create: 2025-06-04 16:32
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/job-config/")
@RequiredArgsConstructor
public class JobConfigController {

    private final JobConfigService jobConfigService;

    /**
     * 获取全部任务配置列表
     *
     * @return 所有任务配置
     */
    @GetMapping("/list")
    public ApiResponse<List<JobConfig>> list() {
        return ApiResponse.success(jobConfigService.list());
    }

    /**
     * 根据ID获取任务配置详情
     *
     * @param id 主键ID
     * @return 对应的任务配置
     */
    @GetMapping("/{id}")
    public ApiResponse<JobConfig> getById(@PathVariable Long id) {
        return ApiResponse.success(jobConfigService.getById(id));
    }

    /**
     * 创建一个新的任务配置
     *
     * @param config 任务配置对象
     * @return 是否创建成功
     */
    @PostMapping
    public ApiResponse<Boolean> save(@RequestBody JobConfig config) {
        return ApiResponse.success(jobConfigService.save(config));
    }

    /**
     * 更新指定 ID 的任务配置
     *
     * @param id     主键ID
     * @param config 更新后的任务配置对象
     * @return 是否更新成功
     */
    @PutMapping("/{id}")
    public ApiResponse<Boolean> update(@PathVariable Long id, @RequestBody JobConfig config) {
        config.setId(id);
        return ApiResponse.success(jobConfigService.updateById(config));
    }

    /**
     * 删除指定 ID 的任务配置
     *
     * @param id 主键ID
     * @return 是否删除成功
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable Long id) {
        return ApiResponse.success(jobConfigService.removeById(id));
    }

    /**
     * 分页查询任务配置，可选条件筛选
     *
     * @param pageNum      当前页码，默认1
     * @param pageSize     每页大小，默认10
     * @param title        按标题模糊查询（可选）
     * @param platformCode 按平台编码精确查询（可选）
     * @param jobType      按任务类型精确查询（可选）
     * @return 分页结果
     */
    @GetMapping
    public ApiResponse<IPage<JobConfig>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String platformCode,
            @RequestParam(required = false) String jobType
    ) {
        Page<JobConfig> page = new Page<>(pageNum, pageSize);
        JobConfig query = new JobConfig();
        query.setTitle(title);
        query.setPlatformCode(platformCode);
        query.setJobType(jobType);

        return ApiResponse.success(jobConfigService.pageJobConfig(page, query));
    }
}
