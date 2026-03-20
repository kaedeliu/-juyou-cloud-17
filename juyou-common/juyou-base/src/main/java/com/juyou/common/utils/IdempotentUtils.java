package com.juyou.common.utils;
//package com.juyou.ticket.common.utils;
//
//import java.util.Arrays;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import com.juyou.ticket.common.sc.RedisUtil;
//import com.juyou.ticket.common.vo.CommonConstant;
//
///**
// * 幂等工具类
// * @author kaedeliu
// *
// */
//@Component
//public class IdempotentUtils {
//
//	@Autowired
//	RedisUtil redisUtil;
//	
//	/**
//	 * 创建token
//	 * @return
//	 */
//	public String creatToken() {
//		String token = UUID.randomUUID().toString().replace("-", "");
//		redisUtil.getStringRedisTemplate().opsForValue().set(CommonConstant.IDEMPORTENT_TOKEN + token, token, 180, TimeUnit.SECONDS);
////	    redisUtil.set(CommonConstant.IDEMPORTENT_TOKEN + token, token);
////	    redisUtil.expire(CommonConstant.IDEMPORTENT_TOKEN + token, 180);
//	    return token;
//	}
//	
//	/**
//	 * 验证幂等
//	 * @param request
//	 * @return
//	 */
//	public boolean verify(HttpServletRequest request) {
//		String token=request.getHeader(CommonConstant.IDEMPORTENT_HEADER_TOKEN);
//		if(!StringUtils.hasLength(token))
//			return false;
//    	String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] " +
//                "then " +
//                "   return  redis.call('del', KEYS[1])" +
//                "else " +
//                "   return -1 " +
//                "end";
//        /**
//         * 使用lua脚本原子验证和删除令牌 判断参数key[1]的值和传入的参数avg[1]是否相等,相等则删除
//         * execute(RedisScript<T> script, List<K> keys, Object... args)
//         * keys: redis中的键名
//         * args: 对比的参数
//         * 返回类型为Long
//         * 返回 0:表示失败
//         *     1:表示成功
//         */
//        Long result = redisUtil.getStringRedisTemplate().execute(new DefaultRedisScript<Long>(luaScript, Long.class),
//                Arrays.asList(CommonConstant.IDEMPORTENT_TOKEN + token),
//                token);
//        if(result == 1){
//            //验证成功,处理业务
//            return true;
//        }
//		
//        //验证失败
//        return false;
//	}
//}
