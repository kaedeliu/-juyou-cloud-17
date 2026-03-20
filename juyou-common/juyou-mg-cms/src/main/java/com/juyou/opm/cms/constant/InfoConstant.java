package com.juyou.opm.cms.constant;

/**
 * 资讯/CMS 相关常量。
 */
public enum InfoConstant {

    COMMON_ATTR("0", "公共属性分类ID");

    /**
     * 为兼容历史代码中的 InfoConstant.COMMON_ATTR_TYPE 直接引用。
     */
    public static final String COMMON_ATTR_TYPE = COMMON_ATTR.code;

    private final String code;
    private final String desc;

    InfoConstant(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
