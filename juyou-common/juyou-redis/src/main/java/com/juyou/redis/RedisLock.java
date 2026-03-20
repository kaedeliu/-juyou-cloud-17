package com.juyou.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 利用reids处理多线程问题
 */
@Component
public class RedisLock {

        private static final String RELEASE_RESULT = "1";

        @Autowired
        RedisUtil redisUtil;

        /**
         * 获取redis锁
         * @param lockKey 锁标识
         * @param requestId 锁的持有者,加锁的请求,可以是线程ID
         * @param expireTime 锁过期时间(秒)
         * @return
         */
        public  boolean getLock(String lockKey, String requestId, long expireTime){
            String script = "if redis.call('EXISTS', KEYS[1]) == 0 then" +
                                "  redis.call('set', KEYS[1] , ARGV[1] ,'EX',"+expireTime+"); " +
                                "  return 1;" +
                            " else  " +
                            "           if redis.call('get', KEYS[1]) == ARGV[1] then" +
                            "                    return 1; " +
                            "           else " +
                            "               return 0; " +
                            "           end " +
                            " end";
//          System.out.println(script);
            Object releaseResult =redisUtil.getRedisTemplate().execute(new DefaultRedisScript<Long>(script,Long.class),
                    Arrays.asList(lockKey),
                    requestId);
            return RELEASE_RESULT.equals(releaseResult.toString());

        }

        /**
         * @功能 尝试获取锁,获取者未当前线程,锁持有时间30秒,等待时间15秒
         * @Author kaedeliu
         * @创建时间 2026/3/18 10:49
         * @修改人 kaedeliu
         * @修改时间 2026/3/18 10:49
         * @Param
         * @param lockKey:
         * @return
        **/
        public boolean tryLock(String lockKey){
                return tryLock(lockKey,Thread.currentThread().getId()+"",30,1);
        }
        /**
         * @功能  尝试获取锁，获取时间不超过wait，每500毫秒获取一次
         * @Author kaedeliu
         * @创建时间 2026/3/18 10:49
         * @修改人 kaedeliu
         * @修改时间 2026/3/18 10:49
         * @Param
         * @param lockKey: 锁标识
         * @param requestId: 锁的持有者
         * @param expireTime: 过期时间，秒
         * @param wait: 等到时间秒
         * @return
        **/
        public boolean tryLock(String lockKey, String requestId, long expireTime,long wait){
            long time=System.currentTimeMillis();
            wait=wait*1000;
            while (System.currentTimeMillis()-wait<time){
                if(!getLock(lockKey,requestId,expireTime)){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    return true;
                }
            }
            return false;
        }

        /**
         * @功能 释放锁,锁的持有者才能释放
         * @Author kaedeliu
         * @创建时间 2026/3/18 10:49
         * @修改人 kaedeliu
         * @修改时间 2026/3/18 10:49
         * @Param
         * @return
        **/
        public boolean releaseLock(String lockKey){
            return releaseLock(lockKey,Thread.currentThread().getId()+"");
        }

        /**
         * 释放锁
         * @param lockKey
         * @param requestId
         * @return
         */
        public  boolean releaseLock(String lockKey, String requestId){
            // 方式1
//        if (jedis.get(lockKey).equals(requestId)) {//校验当前锁的持有人与但概念请求是否相同
//            执行在这里时，如果锁被其它请求重新获取到了，此时就不该删除了
//            jedis.del(lockKey);
//        }

            //方式2
            // eval() 方法会交给redis服务端执行，减少了从服务端再到客户端处理的过程
            //赋值 KEYS[1] = lockKey   ARGV[1] = requestId
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object releaseResult =redisUtil.getRedisTemplate().execute(new DefaultRedisScript<Long>(script,Long.class),
                    Arrays.asList(lockKey),
                   requestId);;
            if (RELEASE_RESULT.equals(releaseResult.toString())) {
                return true;
            }
            return false;
        }
}
