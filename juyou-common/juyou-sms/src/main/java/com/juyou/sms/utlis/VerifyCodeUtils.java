package com.juyou.sms.utlis;

import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.DateUtils;
import com.juyou.redis.RedisUtil;
import com.juyou.sms.config.VerifyCodeConfig;
import com.juyou.sms.dto.SmsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 验证码处理类
 */
public class VerifyCodeUtils {

    @Autowired
    VerifyCodeConfig verifyCodeConfig;

    @Autowired
    RedisUtil redisUtil;

    private static String NowDate = null;


    /**
     * 发送验证码短信
     * @param phone 电话号码
     * @param type 类型，如注册，找回密码
     * @param ip 客户端IP地址
     * @return
     */
    public String sendCode(String phone,String type,String ip){
        VerifyCodeConfig config=getConfig(type);
        if(isIntervalTime(phone,type)){ //验证间隔时间
            throw BaseException.defaultCode("间隔 "+config.getIntervalTime()+" 秒获取一次验证码");
        }
        if(isMobileNum(phone,config.getDayMaxNum())){
            throw BaseException.defaultCode("本日获取验证码次数超过限制");
        }
        if(config.getIpLimit() && StringUtils.hasLength(ip)){ //验证IP是否超了
            if(isIpPhone(ip, phone, config.getIpMaxNum())){
                throw BaseException.defaultCode("本机获取短信验证码次数超限");
            }
        }
        String code=generatedcode(config.getCodeLength());

        String key = SmsConstant.SMS_CODE_CACHE + ":" + type + "_" + phone;
        redisUtil.set(key, code, config.getValidTime()*60);
        String key2 = SmsConstant.SMS_CODETIME_CACHE + ":" + type + "_" + phone;
        redisUtil.set(key2, code, config.getIntervalTime());
        redisUtil.getStringRedisTemplate().opsForHash().increment(SmsConstant.SMS_CODEMOBILE_CACHENUM, phone, 1);
        return code;
    }


    /**
     * 获取配置
     * @param type
     * @return
     */
    private VerifyCodeConfig getConfig(String type){
        if(verifyCodeConfig.getTypeConfig()!=null && verifyCodeConfig.getTypeConfig().containsKey(type)){
            return verifyCodeConfig.getTypeConfig().get(type);
        }
        return verifyCodeConfig;
    }


    /**
     * @功能 判断是否为有效验证码
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @param phone:type_phone
     * @return
     **/
    public boolean isCode(String phone, String type,String writeCode) {
        if(!StringUtils.hasLength(writeCode)) return false;
        return writeCode.equals(getCode(phone,type));
    }

    /**
     * @功能 phone
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @param phone:type_phone
     * @return
     **/
    public String getCode(String phone, String type) {
        String key = SmsConstant.SMS_CODE_CACHE + ":" + type + "_" + phone;
        return (String) redisUtil.get(key);
    }

    /**
     * 是否间隔时间过短
     *
     * @param phone
     * @param type
     * @return
     */
    private boolean isIntervalTime(String phone, String type) {
        String key2 = SmsConstant.SMS_CODETIME_CACHE + ":" + type + "_" + phone;
        return redisUtil.hasKey(key2);
    }

    /**
     * 是否单一手机号超过10次
     *
     * @param phone
     * @return
     */
    private boolean isMobileNum(String phone,Integer maxNum) {
        cleaCacheHash();
        boolean b = redisUtil.hasKey(SmsConstant.SMS_CODEMOBILE_CACHENUM);
        String num = (String) redisUtil.getStringRedisTemplate().opsForHash().get(SmsConstant.SMS_CODEMOBILE_CACHENUM, phone);
        if(!b){ //设置超时时间,直接设置一天，有清除机制
            redisUtil.expire(SmsConstant.SMS_CODEIP_CACHEMOBILE,86400L);
        }
        if (!StringUtils.hasLength(num))
            return false;
        int number = Integer.parseInt(num);
        if (number < maxNum)
            return false;
        else
            return true;
    }



    /**
     * ip获取短信手机超过3个
     *
     * @param ip
     * @return
     */
    private boolean isIpPhone(String ip, String phone, Integer num) {
        if (num == null)
            num = 3;
        cleaCacheHash();
        boolean b=redisUtil.hasKey(SmsConstant.SMS_CODEIP_CACHEMOBILE);
        String mobiles = (String) redisUtil.getStringRedisTemplate().opsForHash().get(SmsConstant.SMS_CODEIP_CACHEMOBILE, ip);
        if(!b){ //设置超时时间,直接设置一天，有清除机制
            redisUtil.expire(SmsConstant.SMS_CODEIP_CACHEMOBILE,86400L);
        }
        if (!StringUtils.hasLength(mobiles))
            return false;
        List<String> mobile = Arrays.asList(mobiles.split(","));
        if (mobile.size() >= num && !mobile.contains(phone))
            return true;
        return false;
    }

    private void cleaCacheHash() {
        if (NowDate == null)
            NowDate = DateUtils.formatDate(new Date(), "yyMMDD");
        else if (!NowDate.equals(DateUtils.formatDate(new Date(), "yyMMDD"))) {
            redisUtil.del(SmsConstant.SMS_CODEIP_CACHEMOBILE);
            redisUtil.del(SmsConstant.SMS_CODEMOBILE_CACHENUM);
        }
    }



    /**
     * 给IP添加次数
     *
     * @param phone
     */
    private void incrementIp(String phone,String ip) {
        if (StringUtils.hasLength(ip)) {
            String mobiles = (String) redisUtil.getStringRedisTemplate().opsForHash().get(SmsConstant.SMS_CODEIP_CACHEMOBILE, ip);
            List<String> mobile = null;
            if (mobiles == null)
                mobile = new ArrayList<>();
            else {
                mobile = new ArrayList<>(Arrays.asList(mobiles.split(",")));
            }
            if (!mobile.contains(phone)) {
                mobile.add(phone);
                redisUtil.getStringRedisTemplate().opsForHash().put(SmsConstant.SMS_CODEIP_CACHEMOBILE, ip, String.join(",", mobile));
            }
        }
    }


    /**
     * @功能 获取指定位数数字
     * @Author kaedeliu
     * @创建时间 2026/3/18 10:49
     * @修改人 kaedeliu
     * @修改时间 2026/3/18 10:49
     * @Param
     * @param count:
     * @return
     **/
    private String generatedcode(int count) {
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * Math.pow(10, count - 1)));
        return code;
    }


}
