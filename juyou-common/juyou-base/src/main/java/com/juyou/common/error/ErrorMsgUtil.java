package com.juyou.common.error;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;



/**
 * 错误工具类
 * @author kaedeliu
 *
 */
@Component
public class ErrorMsgUtil {

	@Autowired
    private EnvUtils envUtils;
	
	/**
	 * code前缀
	 */
	public static final String CODE_START_BUF="emsg.";
	
	
	public static final String CODE="errorCode";
	
	public static final String MSG="errorMsg";
	
	/**
	 * 直接获取消息,只实用与有定义错误的，未定义将返回未知错误
	 * @param key
	 * @return
	 */
	public Result<Object> result(String key){
		String code;
		String errMsg;
		if(StringUtils.isEmpty(key)) {
			errMsg=CommonConstant.COMMON_ERROR_CODE;
			return Result.error(CommonConstant.COMMON_ERROR_CODE, errMsg);
		}
		if(key.startsWith("{")) {
			key=key.replace("{", "").replace("}", "");
		}
		code=key.substring(key.lastIndexOf(".")+1,key.length());
		errMsg=envUtils.env.getProperty(key);
		if(StringUtils.isEmpty(errMsg) && StringUtils.isEmpty(code)) {
			errMsg=envUtils.env.getProperty(CODE_START_BUF+CommonConstant.COMMON_ERROR_CODE);
			return Result.error(CommonConstant.COMMON_ERROR_CODE, errMsg);
		}
		return Result.error(code, errMsg);
	}

	/**
	 * 只实用与有定义错误的，未定义将返回未知错误,抛出异常
	 * @param key 消息配置的key
	 * @param massgae
	 */
	public Result<Object> result(String key,String massgae) {
		String code=null;
		String errMsg; 
		if(StringUtils.isEmpty(key)) {
			code= CommonConstant.COMMON_ERROR_CODE;
		}else {
			if(key.equals(massgae))
				massgae=null;
			key=key.replace("{", "").replace("}", "");
			code=key.substring(key.lastIndexOf(".")+1,key.length());
		}
		
		
		if(!StringUtils.isEmpty(massgae)) {
			return Result.error(code, massgae);
		}else {
			errMsg=envUtils.env.getProperty(key);
			return Result.error(code, errMsg);
		}
		
	}
}
