//package com.juyou.common.gateway.filter;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.juyou.common.constant.CommonConstant;
//import com.juyou.common.env.EnvKey;
//import com.juyou.common.env.EnvUtils;
//import com.juyou.common.error.ErrorCode;
//import com.juyou.common.exception.BaseException;
//import com.juyou.common.jwt.JwtSecret;
//import com.juyou.common.jwt.JwtUtil;
//import com.juyou.common.utils.UrlFilter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
///**
// * jwt拦截验证,
// * @Deprecated 有spring来验证
// * @author kaedeliu
// *
// */
//@Component
//@Slf4j
//@Deprecated
//public class JwtTokenFilter implements GlobalFilter,Ordered{
//
//	@Autowired
//	EnvUtils envUtils;
//
//	@Autowired
//	JwtSecret jwtSecret;
//
////	private static final String CONTENT_TYPE ="Content-Type";
////
////   private static final String CONTENT_TYPE_JSON ="application/json";
////
//	@Override
//	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//		String token = exchange.getRequest().getHeaders().getFirst(CommonConstant.X_ACCESS_TOKEN);
////		String token = getToken(exchange);
//		String url = exchange.getRequest().getURI().getPath();
//		log.info("request url:"+url);
//		if("/health".equals(url) || url.endsWith("favicon.ico")){ //阿里云SLB健康检测
//			ServerHttpResponse response = exchange.getResponse();
//			DataBuffer buffer = response.bufferFactory().wrap("{}".getBytes(StandardCharsets.UTF_8));
//			return response.writeWith(Mono.just(buffer));
//		}
//		String excludeUrls = envUtils.value(EnvKey.JWT放行);
//		if(StringUtils.hasLength(excludeUrls) ) {
//			if(UrlFilter.checkWhiteList(excludeUrls.split(","), url)) {
//				 return chain.filter(exchange);
//			}
//		}
//		if(!StringUtils.hasLength(token)) {
//			throw new BaseException(ErrorCode.A9876);
//		}
//		DecodedJWT jwt=JwtUtil.getJWT(token);
//		String userId = JwtUtil.getUserId(jwt);
//		Integer userType= JwtUtil.getUserType(jwt);
//		Date ct = JwtUtil.getCt(jwt);
//		Date et = JwtUtil.getExpiresAt(jwt);
//
//	    if (userId == null || userType==null || ct==null || et==null) {
//	    	throw new BaseException(ErrorCode.A9876);
////	    	return authErro(resp, ErrorCode.A9102);
//	    }
//	    long expireTime=et.getTime()-ct.getTime();
//
//	    String secret=jwtSecret.getSecrets(userId,userType,expireTime);
//	    if(StringUtils.isEmpty(secret)) {
//	    	throw new BaseException(ErrorCode.A9876);
//	    }
//        if (!JwtUtil.verify(token, secret)) {
////        	log.info("secret error:"+secret);
////        	log.info("secret error:"+token);
//        	throw new BaseException(ErrorCode.A9876);
//        }
//        //token验证通过
//        return chain.filter(exchange);
//	}
//
//	public String getToken(ServerWebExchange exchange) {
//		String token = exchange.getRequest().getHeaders().getFirst(CommonConstant.X_ACCESS_TOKEN);
////		if(!StringUtils.hasLength(token))
////			token=exchange.getAttribute(CommonConstant.X_ACCESS_TOKEN);
//		return token;
//	}
//
//	@Override
//	public int getOrder() {
//		// TODO Auto-generated method stub
//		return 10002;
//	}
//
//}
