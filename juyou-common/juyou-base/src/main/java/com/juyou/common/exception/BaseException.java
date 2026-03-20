package com.juyou.common.exception;

import com.juyou.common.constant.CommonConstant;

public class BaseException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4123793968307516565L;
	
	private String code;
	
	/**
	 * 通过配置文件获取错误
	 * @param code
	 */
	public BaseException(String code) {
		super(code);
		this.code=code;
	}
	
	/**
	 * 自定义消息
	 * @param code
	 * @param message
	 */
	public BaseException(String code,String message) {
		super(message);
		this.code=code;
	}

	public static BaseException defaultCode(String message){
		return new BaseException(CommonConstant.COMMON_ERROR_CODE,message);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
