package com.juyou.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author kaedeliu
 * @Date 2018-07-12 14:23
 * @Desc JWT工具类
 **/
@Slf4j
public class JwtUtil {

	// Token过期时间30天（用户登录过期时间是此时间的两倍，以token在reids缓存时间为准）
//	public static final long EXPIRE_TIME = 60 * 60 * 1000 * 24;

	/**
	 * 校验token是否正确
	 *
	 * @param token  密钥
	 * @param secret 用户的密码
	 * @return 是否正确
	 */
	public static boolean verify(String token, String secret) {
		try {
			// 根据密码生成JWT效验器
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build();
			// 效验TOKEN
			verifier.verify(token);
			return true;
		} catch (Exception exception) {
			log.error("jwt",exception);
			return false;
		}
	}

	
	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static Integer getUserType(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("userType").asInt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static Date getCt(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getClaim("ct").asDate();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 获取创建时间
	 * @param token
	 * @return
	 */
	public static Date getIssuedAt(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getIssuedAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获取超时时间
	 * @param token
	 * @return
	 */
	public static Date getExpiresAt(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt.getExpiresAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获取JWT
	 * @param token
	 * @return
	 */
	public static DecodedJWT getJWT(String token) {
		try {
			DecodedJWT jwt = JWT.decode(token);
			return jwt;
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static String getUserId(DecodedJWT jwt) {
		try {
			return jwt.getClaim("userId").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	public static String getUserId(String token) {
		try {
			return getUserId(JWT.decode(token));
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static Integer getUserType(DecodedJWT jwt) {
		try {
			return jwt.getClaim("userType").asInt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static String getUserName(DecodedJWT jwt) {
		try {
			return jwt.getClaim("userName").asString();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获得token中的信息无需secret解密也能获得
	 *
	 * @return token中包含的Id
	 */
	public static Date getCt(DecodedJWT jwt) {
		try {
			return jwt.getClaim("ct").asDate();
		} catch (JWTDecodeException e) {
			return null;
		}
	}

	/**
	 * 获取创建时间
	 * @param jwt
	 * @return
	 */
	public static Date getIssuedAt(DecodedJWT jwt) {
		try {
			return jwt.getIssuedAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 获取超时时间
	 * @param jwt
	 * @return
	 */
	public static Date getExpiresAt(DecodedJWT jwt) {
		try {
			return jwt.getExpiresAt();
		} catch (JWTDecodeException e) {
			return null;
		}
	}
	
	/**
	 * 生成签名
	 * 因多服务器间有时间差，所有生成签名提前30分钟，并记录生成token服务器时间，用于获取secret
	 * @param userId 用户编号
	 * @param secret   用户的密码
	 * @param expireTime 过期时间,毫秒
	 * @return 加密的token 
	 */
	public static String sign(String userId,Integer userType,long expireTime, String secret) {
		return sign(userId,userType,expireTime,secret,null);
	}
	/**
	 * 生成签名
	 * 因多服务器间有时间差，所有生成签名提前30分钟，并记录生成token服务器时间，用于获取secret
	 * @param userId 用户编号
	 * @param secret   用户的密码
	 * @param expireTime 过期时间,毫秒
	 * @param userName 用户名
	 * @return 加密的token
	 */
	public static String sign(String userId,Integer userType,long expireTime, String secret,String userName) {
		Date now=new Date();
		Date date = new Date(now.getTime() + expireTime);
		Algorithm algorithm = Algorithm.HMAC256(secret);
		// 附带userCode信息
		JWTCreator.Builder builder= JWT.create();
		if(StringUtils.hasLength(userName))
			builder.withClaim("userName",userName);
		return builder.withClaim("userId", userId).withClaim("userType", userType).withClaim("ct", new Date()).withIssuedAt(new Date(System.currentTimeMillis()-30*60*1000L)).withExpiresAt(date).sign(algorithm);

	}

	/**
	 * 根据request中的token获取用户账号
	 * 
	 * @param request
	 * @return
	 * @throws BaseException
	 */
	public static String getUserIdByToken(HttpServletRequest request) throws BaseException {
		String accessToken = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
		DecodedJWT jwt = JWT.decode(accessToken);
		return getUserId(jwt);
	}
	
//	/**
//	 * 根据request中的token获取用户id
//	 * 
//	 * @param request
//	 * @return
//	 * @throws JeecgBootException
//	 */
//	public static String getUserSeqByToken(HttpServletRequest request) throws BaseException {
//		String accessToken = request.getHeader("X-Access-Token");
//		String userSeq = getUserSeq(accessToken);
//		return userSeq;
//	}

	public static String getTokenByRequest(HttpServletRequest request){
		String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
		//改造，接口token在request属性里面
		if(!StringUtils.hasLength(token))
			token=(String) request.getAttribute(CommonConstant.X_ACCESS_TOKEN);
		return token;
	}
	
	
	
//	/**
//	  *  从session中获取变量
//	 * @param key
//	 * @return
//	 */
//	@SuppressWarnings("deprecation")
//	public static String getSessionData(String key) {
//		//${myVar}%
//		//得到${} 后面的值
//		String moshi = "";
//		if(key.indexOf("}")!=-1){
//			 moshi = key.substring(key.indexOf("}")+1);
//		}
//		String returnValue = null;
//		if (key.contains("#{")) {
//			key = key.substring(2,key.indexOf("}"));
//		}
//		if (!StringUtils.isEmpty(key)) {
//			HttpSession session = SpringContextUtils.getHttpServletRequest().getSession();
//			returnValue = (String) session.getAttribute(key);
//		}
//		//结果加上${} 后面的值
//		if(returnValue!=null){returnValue = returnValue + moshi;}
//		return returnValue;
//	}
	
	public static void main(String[] args) {
//		System.out.println(JwtUtil.sign("test", 1,10000L,"123456"));
//		String token=JwtUtil.sign("123456", 1,360000L,"9987");
//		String token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyVHlwZSI6MCwiZXhwIjoxNjI4MDkyNDA5LCJpYXQiOjE2MjgwODg4MDksInVzZXJDb2RlIjoiNjU0MzIxIn0.3ospFC8ibNmGT9Q4SX2ukks8_Occ1eiwcfb8U6LyxLk";
//		System.out.println(token);
////		try {
////			Thread.sleep(10000);
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//		System.out.println(JwtUtil.verify(token, "8987"));
		String userCode="1111";
		Integer userType=2;
		Long time=15*60*1000l;
		String key="jwtSecret%$&1";
		String secrets=JwtSecret.getSecrets(userCode,userType,time,key);
		String token=sign(userCode,userType,time,key);
		System.out.println(token);
	}
	
}
