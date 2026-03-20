package com.juyou.sms.utlis;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.teaopenapi.models.Config;
import com.juyou.sms.config.AliSmsConfig;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/
@Component
@Slf4j
public class AliSmsUtils {

    @Autowired
    AliSmsConfig aliSmsConfig;

    /**
     * 使用AK&SK初始化账号Client
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    private  com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        Config config = new Config()
                // 您的AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 您的AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = aliSmsConfig.getEndpoint();
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    /**
     * 发送短信
     * @param templateId 模板ID
     * @param phoneNumberSet 手机号
     * @param templateParamSet 参数列表
     * @throws Exception
     */
    public void sendSms(String templateId, String[] phoneNumberSet, Map<String,String> templateParamSet) throws Exception {
        JSONObject json=new JSONObject();
        json.put("TemplateCode",templateId);
        json.put("PhoneNumbers",String.join(",",phoneNumberSet));
        if(templateParamSet!=null && !templateParamSet.isEmpty())
            json.put("TemplateParam",JSONObject.toJSONString(templateParamSet));
        sendMsg(json);
    }

    /**
     * @功能 发送短信，同步
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @param json:
     * @return
    **/
    public  void sendMsg(JSONObject json) throws Exception {
        com.aliyun.dysmsapi20170525.Client client = createClient(aliSmsConfig.getSecretId(),aliSmsConfig.getSecretKey());
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(json.getString("PhoneNumbers"));
        sendSmsRequest.setSignName(aliSmsConfig.getSignName());
        sendSmsRequest.setTemplateCode(json.getString("TemplateCode"));
        if(json.getJSONObject("TemplateParam")!=null){
            sendSmsRequest.setTemplateParam( json.getJSONObject("TemplateParam").toJSONString());
        }
        log.info("发送短信:"+json.toJSONString());
        // 复制代码运行请自行打印 API 的返回值
        SendSmsResponse response=client.sendSms(sendSmsRequest);
        log.info("发送短信结果:"+JSONObject.toJSONString(response.getBody()));
    }

    /**
     * @功能 发送短信，异步
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @param json:
     * @return
    **/
    public  void asyncSendMsg(JSONObject json) throws Exception{
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliSmsConfig.getSecretId())
                .accessKeySecret(aliSmsConfig.getSecretKey())
                //.securityToken("<your-token>") // use STS token
                .build());
        AsyncClient client = null;
        try {
            client=AsyncClient.builder()
                    .region(aliSmsConfig.getRegionName()) // Region ID
                    .credentialsProvider(provider)

                    .overrideConfiguration(
                            ClientOverrideConfiguration.create()
                                    .setEndpointOverride(aliSmsConfig.getEndpoint())
                    )
                    .build();

            com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest.Builder builder = com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest.builder()
                    .phoneNumbers(json.getString("PhoneNumbers"))
                    .signName(aliSmsConfig.getSignName())
                    .templateCode(json.getString("TemplateCode"));
            if(json.getJSONObject("TemplateParam")!=null){
                builder.templateParam( json.getJSONObject("TemplateParam").toJSONString());
            }

            com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest sendSmsRequest=builder.build();

            log.info("发送短信:"+json.toJSONString());

            CompletableFuture< com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse> response = client.sendSms(sendSmsRequest);
            com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse resp = response.get();
            log.info("发送短信结果:"+JSONObject.toJSONString(resp));
        }catch (Exception e){}
        finally {
            if(client!=null)
                client.close();
        }

    }
}