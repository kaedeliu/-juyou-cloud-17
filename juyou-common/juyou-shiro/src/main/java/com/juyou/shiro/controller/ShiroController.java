package com.juyou.shiro.controller;


import com.juyou.common.error.ErrorCode;
import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author kaedeliu
 * 登录超市或者无权限输出
 */
@RequestMapping("/sys/common")
@RestController
public class ShiroController {

	@RequestMapping("/403")
	public Result<Object> unauthorized() {
		throw new BaseException(ErrorCode.A9102);
//		return Result.error(CommonConstant.SHIRO_HTTP_403, "登录超时");
	}
	
	@RequestMapping("/noLogin")
	public Result<Object> noLogin() {
		throw new BaseException(ErrorCode.A9102);
//		return Result.error(CommonConstant.SHIRO_NO_LOGIN, "未登录");
	}
}
