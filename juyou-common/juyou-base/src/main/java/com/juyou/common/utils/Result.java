package com.juyou.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.core.env.Environment;

import java.io.Serializable;

/**
 *   接口返回数据格式
 * @author scott
 * @email jeecgos@163.com
 * @date  2019年1月19日
 */
@Data
@Schema(title ="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	/*1~*/@Schema(title = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	/*1~*/@Schema(title = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	/*1~*/@Schema(title = "返回代码")
	private String code = "00000";
	
	/**
	 * 返回数据对象 data
	 */
	/*1~*/@Schema(title = "返回数据对象")
	private T result;
	
	/**
	 * json过滤器
	 */
	@JSONField(serialize = false)
	@JsonIgnore
	private Object filter;
	
	@JSONField(serialize = false)
	@JsonIgnore
    private Environment env;
	
	
	/**
	 * 用作流输出开始
	 */
	@JSONField(serialize = false)
	public static byte[] streamBegin() {
		return "{\"success\":true,\"code\":\"00000\",\"result\":".getBytes();
	}
	
	/**
	 * 用着流输出结束
	 * @return
	 */
	@JSONField(serialize = false)
	public static byte[] streamEnd() {
		return "}".getBytes();
	}
	
	
	/**
	 * 时间戳
	 */
	/*1~*/@Schema(title = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {
		
	}
	
	public Result(T result) {
		this.result=result;
	}
	public Result<T> success() {
		this.success = true;
		return this;
	}
	
	public  Result<T> success(String message) {
		this.message = message;
		this.success = true;
		return this;
	}

	public  Result<T> success(String message,String code) {
		this.message = message;
		this.code = code;
		this.success = true;
		return this;
	}
	

	public static<T> Result<T> OK() {
		Result<T> r = new Result<T>();
		return r;
	}

	public static<T> Result<T> OK(T data) {
		Result<T> r = new Result<T>();
		r.setResult(data);
		return r;
	}

	public static<T> Result<T> OK(String msg, T data) {
		Result<T> r = new Result<T>();
		r.setSuccess(true);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}
	
	
	public static Result<Object> error(String msg) {
		return error(null, msg);
	}
	
	public static Result<Object> error(String code, String msg) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public static <T> Result<T> error(String code, String msg,Class<T> clazz)  {
		Result<T> r = new Result<T>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}
	
	public String toJsonString() {
		return JSONObject.toJSONString(this);
	}
	
	/**
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static Result<Object> errorTemp(String code, String msg) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}
	
}