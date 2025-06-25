package org.caesar.media.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private String path;
    private String component;
    private String redirect;
    private String name;
    private MetaVo meta;
    private List<MenuVo> children;
}
