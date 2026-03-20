package com.juyou.shiro;

import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisClusterManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.*;

/**
 * @author: lj
 * @date: 2021/2/7
 * @description: shiro 配置类
 */

@Slf4j
@Configuration
public class ShiroConfig {

    @Resource
    LettuceConnectionFactory lettuceConnectionFactory;
     
    @Autowired
    private EnvUtils envUtils;


    
    /**
     * Filter Chain定义说明
     *
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     * 4、如果使用配置中心，修改代码需要在ShiroConfigUpdate中同步修改
     */
	@Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager( securityManager);
        // 拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        String excludeUrls = envUtils.value(EnvKey.SHIRO放行);
        if(!StringUtils.isEmpty(excludeUrls)){
            String[] permissionUrl = excludeUrls.split(",");
            for(String url : permissionUrl){
            	if(StringUtils.hasLength(url))
            		filterChainDefinitionMap.put(url , "anon");
            }
        }

        // Swagger/Knife4j 放行（否则访问聚合文档会被重定向/拒绝）
        // 网关通常会 StripPrefix=1，把 /admin/v2/api-docs 转成后端的 /v2/api-docs
        String[] swaggerAnonUrls = new String[]{
                "/v2/api-docs",
                "/v2/api-docs/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                // 有的链路可能不 StripPrefix，把 /admin/app/order 前缀保留下来
                "/admin/v2/api-docs",
                "/admin/v2/api-docs/**",
                "/app/v2/api-docs",
                "/app/v2/api-docs/**",
                "/order/v2/api-docs",
                "/order/v2/api-docs/**",
                "/admin/v3/api-docs",
                "/admin/v3/api-docs/**",
                "/app/v3/api-docs",
                "/app/v3/api-docs/**",
                "/order/v3/api-docs",
                "/order/v3/api-docs/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/doc.html",
                "/admin/swagger-ui.html",
                "/admin/swagger-ui/**",
                "/admin/doc.html",
                "/app/swagger-ui.html",
                "/app/swagger-ui/**",
                "/app/doc.html",
                "/order/swagger-ui.html",
                "/order/swagger-ui/**",
                "/order/doc.html",
                "/knife4j/**"
        };
        for (String url : swaggerAnonUrls) {
            if (StringUtils.hasLength(url)) {
                filterChainDefinitionMap.put(url, "anon");
            }
        }
        
        // 添加自己的过滤器并且取名为jwt
        Map<String, Filter> filterMap = new HashMap<String, Filter>(1);
        //如果cloudServer为空 则说明是单体 需要加载跨域配置
        boolean cros=envUtils.value(EnvKey.SHIROCros跨域,Boolean.class);
//        Object cloudServer = envUtils.env.getProperty(CommonConstant.CLOUD_SERVER_KEY);
        filterMap.put("jwt", new JwtFilter(cros));
        shiroFilterFactoryBean.setFilters(filterMap);

        filterChainDefinitionMap.put("/**", "jwt");
        
        // 未授权界面返回JSON
        shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
        shiroFilterFactoryBean.setLoginUrl("/sys/common/noLogin");
        
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(ShiroRealm myRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        

//        //YKX平台关闭缓存
//        myRealm.setCachingEnabled(false);
        
        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-
         * StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        
//        DefaultWebSessionManager defaultSessionManager = new DefaultWebSessionManager();
//        //将sessionIdUrlRewritingEnabled属性设置成false
//        defaultSessionManager.setSessionIdUrlRewritingEnabled(false);
//
//        securityManager.setSessionManager(defaultSessionManager);

       

        //自定义缓存实现,使用redis
        securityManager.setCacheManager(redisCacheManager());
        securityManager.setRealm(myRealm);
        
        return securityManager;
    }

    /**
     * 下面的代码是添加注解支持
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        /**
         * 解决重复代理问题 github#994
         * 添加前缀判断 不匹配 任何Advisor
         */
//        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
//        defaultAdvisorAutoProxyCreator.setAdvisorBeanNamePrefix("_no_advisor");
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager redisCacheManager() {
        log.info("===============(1)创建缓存管理器RedisCacheManager");
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        //redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("userId");
        //用户权限信息缓存时间
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */ 
	@Bean
    public IRedisManager redisManager() {
        log.info("===============(2)创建RedisManager,连接Redis..");
        IRedisManager manager;
        // redis 单机支持，在集群为空，或者集群无机器时候使用 add by jzyadmin@163.com
        if (lettuceConnectionFactory.getClusterConfiguration() == null || lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().isEmpty()) {
            RedisManager redisManager = new RedisManager();
            redisManager.setHost(lettuceConnectionFactory.getHostName()+":"+lettuceConnectionFactory.getPort());
//            redisManager.setPort(lettuceConnectionFactory.getPort());
            redisManager.setTimeout(0);
            if (!StringUtils.isEmpty(lettuceConnectionFactory.getPassword())) {
                redisManager.setPassword(lettuceConnectionFactory.getPassword());
            }
            manager = redisManager;
        }else{
            // redis 集群支持，优先使用集群配置	add by jzyadmin@163.com
            RedisClusterManager redisManager = new RedisClusterManager();
            Set<HostAndPort> portSet = new HashSet<>();
            lettuceConnectionFactory.getClusterConfiguration().getClusterNodes().forEach(node -> portSet.add(new HostAndPort(node.getHost() , node.getPort())));
            JedisCluster jedisCluster = new JedisCluster(portSet);
            redisManager.setJedisCluster(jedisCluster);
            manager = redisManager;
        }
        return manager;
    }

}
