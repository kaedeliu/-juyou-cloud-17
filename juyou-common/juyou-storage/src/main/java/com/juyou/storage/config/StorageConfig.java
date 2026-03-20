package com.juyou.storage.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 *storage配置
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.storage")
@Component
public class StorageConfig {

    /**
     * 存储模式,local:本地,alioss:阿里云,txcos:腾讯cos
     */
    String storageType="local";

    /**
     * 最大上传限制，值为XMB,XKB,XGB,默认5MB
     */
    String maxLen="5MB";

    /**
     * 根据类型限制，值为XMB,XKB,XGB,默认5MB，当无此类型配置时，使用maxLen限制
     * image/jpeg: 5MB
     */
    Map<String,String> typeMaxLen;

    /**
     * 0:原文件名,1时间戳+随机数，2:日期加随机数,3:yyyyMM/yyyyMMddHHmmssSSS+sj+后缀
     * 默认3
     */
    Integer fileNameType=3;


}
