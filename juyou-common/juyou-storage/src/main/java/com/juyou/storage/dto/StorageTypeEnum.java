package com.juyou.storage.dto;

/**
 * 上传类型配置
 */
public enum StorageTypeEnum {

    本地存储("local"),
    阿里云OSS("alioss"),
    腾讯COS("txcos");

    public String val;

    StorageTypeEnum(String val){
        this.val=val;
    }
}
