package org.caesar.media.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 检查session是否有效果
 * @Author: peng.guo
 * @Create: 2025-06-06 15:47
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckSessionDto {

    private boolean valid;
    private String message;
}
