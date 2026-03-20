package com.juyou.sms.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.sms")
@Component
public class SmsConfig {

    /**
     * 发送短信方式,ali,tx
     * 默认值ali
     */
    String type="ali";
}
