//package com.juyou.common.config;
//
//import com.juyou.common.base.service.BaseService;
//import com.juyou.common.constant.CommonConstant;
//import com.juyou.common.env.EnvKey;
//import com.juyou.common.env.EnvUtils;
//import com.juyou.common.error.ErrorCode;
//import com.juyou.common.exception.BaseException;
//import com.juyou.common.jwt.JwtSecret;
//import com.juyou.common.jwt.JwtUtil;
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.lang.reflect.Method;
//
///**
// * @功能 通讯时注入权限认证
// * @Author kaedeliu
// * @创建时间 2026/3/18 10:49
// * @修改人 kaedeliu
// * @修改时间 2026/3/18 10:49
// * @Param
// * @return
// **/
//@Configuration
//@Slf4j
//public class FeignConfig implements RequestInterceptor {
//
//	@Autowired
//	EnvUtils envUtils;
//	@Autowired
//	BaseService baseService;
//	@Autowired
//	JwtSecret jwtSecret;
//
//
//
//
//	/**
//	 * tonken过期时间，毫秒
//	 */
//	static final long TOKEN_TIME = 1 * 30 * 1000L;
//
//	@Override
//	public void apply(RequestTemplate requestTemplate) {
//		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = requestAttributes.getRequest();
////		if (request.getHeader(CommonConstant.X_ACCESS_TOKEN) != null) {
////			log.info("我有token");
////			if(JwtUtil.getUserType(request.getHeader(CommonConstant.X_ACCESS_TOKEN))!= StatusEnum.USER_TYPE_客户端.getCode()){
////				log.info("我不是客户端toke");
////				requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, request.getHeader(CommonConstant.X_ACCESS_TOKEN));
////				return;
////			}
////		}
//		Method method = requestTemplate.methodMetadata().method();
//		CreareToken token = method.getAnnotation(CreareToken.class);
//		log.info("我有token:"+token +" method："+method.getName());
//		if (token != null) {
//			requestTemplate.header(CommonConstant.X_ACCESS_TOKEN, createToken());
//		}
//
//		requestTemplate.header("cookie", request.getHeader("cookie"));
//	}
//
//	/**
//	 * @功能 创建一个通讯可以使用的token
//	 * @Author kaedeliu
//	 * @创建时间 2026/3/18 10:49
//	 * @修改人 kaedeliu
//	 * @修改时间 2026/3/18 10:49
//	 * @Param
//	 * @return
//	 **/
//	private String createToken() {
//		String openid = envUtils.value(EnvKey.FEIGN_OPENID);
//		LoginUserDto openUser = baseService.queryLoginUserInfo(openid);
//		if (openUser == null) {
//			throw new BaseException(ErrorCode.A9999);
//		}
//
//		String secret = jwtSecret.getSecrets(openid, openUser.getUserType(), TOKEN_TIME);
//		String tokenItem = JwtUtil.sign(openid, openUser.getUserType(), TOKEN_TIME, secret);
//		return tokenItem;
//	}
//
//
//}
