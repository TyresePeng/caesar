package org.caesar.media.vo;

import lombok.Data;

@Data
public class MetaVo {
    private String title;
    private String icon;
    private Boolean hidden;
    private Boolean alwaysShow;
    private Boolean keepAlive;
    private Object params;

}
