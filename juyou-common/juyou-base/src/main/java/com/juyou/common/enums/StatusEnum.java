package com.juyou.common.enums;

/**
 * 全局状态、类型定义
 *
 * @author kaedeliu
 */
public enum StatusEnum {

    是("通用是", 1),
    否("通用否", 0),


    //用户类型user_type
    USER_TYPE_系统超管("系统超管", -1),
    USER_TYPE_默认("默认", 0),
    USER_TYPE_客户端("客户端", 1),
    USER_TYPE_接口用户("接口用户", 2),


    系统类型_默认("默认",0),
    系统类型_预留("预留",1),
    ;

    // 最后面要以分号结束


    private String desc;
    private Integer val;

    // 构造方法
    private StatusEnum(String desc, Integer val) {
        this.desc = desc;
        this.val = val;
    }

    // 返回描述内容（K）
    public String getDesc() {
        return desc;
    }

    // 返回的值（V）
    public int getCode() {
        return val;
    }

    public static StatusEnum val(Integer val) {
        for (StatusEnum statusEnum : values()) {
            if (statusEnum.getCode() == val) {
                return statusEnum;
            }
        }
        return null;
    }

}