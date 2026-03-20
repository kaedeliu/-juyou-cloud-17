package com.juyou.storage.config;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.storage.cos")
@Component
public class CosConfig {

    /**
     * 腾讯cos secretId
     */
    String secretId;

    /**
     * 腾讯 secretKey
     */
    String secretKey;

    /**
     * 腾讯 regionName
     */
    String regionName;

    /**
     * 存储桶bucketName
     */
    String bucketName;

    /**
     * 请求根路径，即上传得目录
     */
    String rootPath="";

    /**
     * 存储得频率， 默认是标准(Standard), 低频(standard_ia)
     */
    String standardClass="Standard";

    /**
     * 0:相对路径，1根据returnPath格式化返回，可以返回请求路径
     */
    Integer returnType=1;

    /**
     * 格式化返回如/view?paht=[filePath],
     * 支持参数filePath,fileName,regionName,bucketName,rootPath
     */
    String returnPath;


}
