package com.juyou.storage.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.storage.local")
@Component
public class LocalConfig {

    /**
     * 存储路径
     */
    String rootPath;

    /**
     * 0:相对路径，1根据returnPath格式化返回，可以返回请求路径
     */
    Integer returnType=1;

    /**
     * 格式化返回如/view?paht=[filePath],支持参数filePath
     */
    String returnPath;
}
