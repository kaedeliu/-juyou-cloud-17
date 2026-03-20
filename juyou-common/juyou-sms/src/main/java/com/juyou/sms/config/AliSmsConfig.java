package com.juyou.sms.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.sms.ali")
@Component
public class AliSmsConfig {


    String secretId;

    String secretKey;

    /**
     * 腾讯 regionName
     */
    String regionName;

    String sdkAppId;

    String signName;

    /**
     * 接入域名
     */
    String endpoint="dysmsapi.aliyuncs.com";
}
