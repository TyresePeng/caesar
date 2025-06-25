package org.caesar.media.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 平台类型
 */
@Getter
@AllArgsConstructor
public enum PlatformType {

    /**
     * 平台code
     */
    DOUYIN("douyin"),
    WECHAT("wechat"),
    HONSHU("xiaohonshu");
    private final String value;
}
