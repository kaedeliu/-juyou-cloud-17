package com.juyou.storage.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里OSS配置
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.storage.oss")
@Component
public class OssConfig {

    String bucketName;

    String endpoint;

    String accessKeySecret;

    String accessKeyId;

    String rootPath="";

    /**
     * 0:相对路径，1根据returnPath格式化返回，可以返回请求路径
     */
    Integer returnType=1;

    /**
     * 格式化返回如/view?paht=[filePath],
     * 支持参数filePath,fileName,bucketName,rootPath
     */
    String returnPath;
}
