package com.juyou.storage.dto;

/**
 * 上传类型配置
 */
public enum StorageFileNameTypeEnum {

    原文件名(0),
    时间戳(1),
    日期_随机数(2),
    文件夹_日期_随机数(3);
    public Integer val;

    private StorageFileNameTypeEnum(Integer val){
        this.val=val;
    }
}
