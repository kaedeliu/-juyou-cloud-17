package com.juyou.sms.utlis;

import com.juyou.sms.config.SmsConfig;
import com.juyou.sms.dto.SmsTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

/**
 * 发送短息工具类
 */
@Component
public class SmsUtils {

    @Autowired
    SmsConfig smsConfig;

    @Autowired
    AliSmsUtils aliSmsUtils;

    @Autowired
    TxSmsUtils txSmsUtils;

    @Autowired
    VerifyCodeUtils verifyCodeUtils;

    /**
     * 发送短信
     * @param templateId 模板ID
     * @param phoneNumberSet 短信列表
     * @param templateParamSet 参数，需注意，腾讯SMS参数为有序的，按值形式插入
     * @throws Exception
     */
    public  void sendSms(String templateId, String[] phoneNumberSet, LinkedHashMap<String,String> templateParamSet) throws Exception {
        if(SmsTypeEnum.腾讯.val.equals(smsConfig.getType())){
            txSmsUtils.sendSms(templateId,phoneNumberSet,templateParamSet.values().toArray(new String[0]));
        }else{
            aliSmsUtils.sendSms(templateId,phoneNumberSet,templateParamSet);
        }
    }

    /**
     * 发送验证码，自动处理存储时间等
     * @param phone 手机号
     * @param type 发送类型，相互约定，如注册，找回密码
     */
    public String verifyCode(String phone,String type,String ip){
        return verifyCodeUtils.sendCode(phone,type,ip);
    }

    /**
     * 验证验证码是否有效
     * @param phone 电话好嘛
     * @param type 类型
     * @param writeCode 输入的code
     * @return
     */
    public boolean isCode(String phone, String type,String writeCode){
        return verifyCodeUtils.isCode(phone,type,writeCode);
    }

//    public static void main(String[] args) {
//        LinkedHashMap<String,String> templateParamSet =new LinkedHashMap<>();
//        templateParamSet.put("1","v1");
//        templateParamSet.put("2","v2");
//        String[] vs=templateParamSet.values().toArray(new String[0]);
//        System.out.println(JSONObject.toJSONString(vs));
//    }
}
