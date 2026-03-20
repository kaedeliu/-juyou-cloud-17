package com.juyou.sms.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.sms.verifyCode")
@Component
public class VerifyCodeConfig {

    /**
     * 验证码长度
     * 默认6
     */
    Integer codeLength=6;

    /**
     * 有效时间，单位分钟
     * 默认15
     */
    Integer validTime=15;

    /**
     * 每日最大发送数
     * 默认10，更大需要短信方同步配置
     */
    Integer dayMaxNum=10;

    /**
     * 间隔时间，单位秒
     * 默认值60
     */
    Integer intervalTime=60;

    /**
     * 是否开启IP限制
     */
    Boolean ipLimit=false;

    /**
     * Ip限制次数
     */
    Integer ipMaxNum=3;


    Map<String,VerifyCodeConfig> typeConfig;
}
