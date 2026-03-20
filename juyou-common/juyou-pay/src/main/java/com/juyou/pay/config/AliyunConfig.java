package com.juyou.pay.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Accessors(chain = true)
@ConfigurationProperties("cdyx.ali.pay")
@Component
public class AliyunConfig {

//    final static  String prefix="cdyx.ali.pay.";

    @Autowired
    public Environment env;


    // 1.商户appid
    public  String appId;


    // 2.私钥 pkcs8格式的
    public  String  rsaPrivatekey;

    // 3.支付宝公钥
    public  String  alipayPublicKey;
    /**
     * 应用公钥
     */
    public  String appPublicKey;

    // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public  String  notifyUrl;

//
//    // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public  String notify_urlAPP_activtiy = IPADDRESS + "/buddha/payResultController/aliyunAppPayResult_activtiy";
//
//    // 4.服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public  String notify_urlH5 = IPADDRESS + "/buddha/payResultController/aliyunPayH5PayResult";

    // 5.页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public  String returnUrl;

    // 6.请求网关地址
    //https://openapi.alipaydev.com/gateway.do
    public  String url = "https://openapi.alipay.com/gateway.do";

    // 7.编码
    public  String  charset= "UTF-8";

    // 8.返回格式
    public  String  format= "json";

    // 9.加密类型
    public  String signType= "RSA2";


    /**
     * 用户中途取消支付回调
     */
    public  String  quitUrl;

    public  String sellerId;
//
//    public String getAppId() {
//        return env.getProperty(prefix+"appId");
//    }
//
//    public String getRsaPrivatekey() {
//        return env.getProperty(prefix+"rsaPrivatekey");
//    }
//
//    public String getAlipayPublicKey() {
//        return env.getProperty(prefix+"alipayPublicKey");
//    }
//
//    public String getAppPublicKey() {
//        return env.getProperty(prefix+"appPublicKey");
//    }
//
//    public String getNotifyUrl() {
//        return env.getProperty(prefix+"notifyUrl");
//    }
//
//    public String getReturnUrl() {
//        return env.getProperty(prefix+"returnUrl");
//    }
//
//    public String getUrl() {
//        return env.getProperty(prefix+"url");
//    }
//
//    public String getCharset() {
//        return env.getProperty(prefix+"charset");
//    }
//
//    public String getFormat() {
//        return env.getProperty(prefix+"format");
//    }
//
//    public String getSignType() {
//        return env.getProperty(prefix+"signType");
//    }
//
//    public String getQuitUrl() {
//        return env.getProperty(prefix+"quitUrl");
//    }
//
//    public String getSellerId() {
//        return env.getProperty(prefix+"sellerId");
//    }
}
