package com.juyou.common.jwt;

import com.juyou.common.enums.StatusEnum;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.sign.MD5Util;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



/**
 * 原则上每日一环，保存7天
 * @author kaedeliu
 *
 */
@Component
public class JwtSecret {

	static ConcurrentMap<String,String> secrets=new ConcurrentHashMap<String, String>();
	/**
	 * 当前日期
	 */
	static String today=null;
	
	/**
	 * 有效期,天
	 */
	static final int secretExp=7;
	
	static final String format="yyyyMMdd";
	
	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	EnvUtils envUtils;
	
	
//	/**
//	 * 通过日期获取secret，secret必须已经生成
//	 * @param date
//	 * @return
//	 */
//	public String getSecrets(Date date) {
//		Date now=new Date();
//		int day=DateUtils.differentDays(date, now);
//		if(day>secretExp) {
//			return null;
//		}
//		
//		String d=DateUtils.formatDate(date, format);
//		String secret=secrets.get(d);
//		if(StringUtils.hasLength(secret))
//			return secret;
//		secret = (String) redisUtil.get(CommonConstant.SYS_CACHE_JWT_SECRET+d);
//		if(StringUtils.hasLength(secret))
//			secrets.put(d, secret);
//		return secret;
//	}
	

	/**
	 * 获取一个新的secret
	 * @return
	 */
	public String getSecrets(String userCode,Integer userType,long expireTime) {
		//使用算法算key
		String key=null;
		if (userType == StatusEnum.USER_TYPE_默认.getCode()) {
			key=envUtils.value(EnvKey.登录Secrets);
		}else if(userType==  StatusEnum.USER_TYPE_客户端.getCode()){
			key=envUtils.value(EnvKey.客户端登录Secrets);
		}
		return getSecrets(userCode,userType,expireTime,key);
//		Date now=new Date();
//		String d=DateUtils.formatDate(now, format);
//		if(d.equals(today)) //表示已经生成
//			return getSecrets(now);
//		else {
//			String secret =  getSecrets(now);
//			if(StringUtils.hasLength(secret)) return secret; 
//			secret = Math.round(Math.random()*10000)+"";
//			redisUtil.set(CommonConstant.SYS_CACHE_JWT_SECRET+d, secret,secretExp * 24 * 60 *60L);
//			secrets.put(d, secret);
//			today=d;
//			return secret;
//		}
	}

	public static String getSecrets(String userCode,Integer userType,long expireTime,String key){
		String e=expireTime+"";
		e=e.substring(e.length()-1,e.length());
		e=expireTime+"".substring(0,Integer.parseInt(e));
		String signStr=userCode+userType+expireTime+e+key;
		return MD5Util.MD5Encode(signStr, "UTF-8");
	}
	

}
