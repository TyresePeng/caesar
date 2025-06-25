package org.caesar.media.dto;

import lombok.Data;

/**
 * @Description: 平台用户dto
 * @Author: peng.guo
 * @Create: 2025-06-03 16:19
 * @Version 1.0
 **/
@Data
public class PlatformUserDto {

    /**
     * 排序
     */
    private Integer sort;
    /**
     * 平台code
     */
    private String code;

    /**
     * 平台用户名称
     */
    private String name;
}
