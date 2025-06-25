package org.caesar.media.dto;

import lombok.Data;

/**
 * @Description: 分页查询
 * @Author: peng.guo
 * @Create: 2025-06-03 16:44
 * @Version 1.0
 **/
@Data
public class PageDto {

    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;

}
