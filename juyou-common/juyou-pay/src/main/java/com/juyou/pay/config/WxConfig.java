package com.juyou.pay.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.pay.wx")
@Component
public class WxConfig {

//    final static  String prefix="cdyx.pay.wx.";

//    @Autowired
//    public Environment env;

    String appId;
    String mchId;
    String mchKey;
    String tradeType;
    String appName;
    String notifyUrl;
    String keyPath;

    /**
     * 微信支付API版本号，默认V3,V2代码已经不支持
     */
    String version="V3";
//
//    public String getAppId(){
//        return env.getProperty(prefix+"appId");
//    }
//
//    public String getMchId(){
//        return env.getProperty(prefix+"mchId");
//    }
//
//    public String getMchKey(){
//        return env.getProperty(prefix+"key");
//    }
//
//    public String getTradeType(){
//        return env.getProperty(prefix+"tradeType");
//    }
//    public String getAppName(){
//        return env.getProperty(prefix+"appName");
//    }
//
//
//    public String getNotifyUrl(){
//        return env.getProperty(prefix+"notifyUrl");
//    }
//    public String getKeyPath(){
//        return env.getProperty(prefix+"keyPath");
//    }


}
