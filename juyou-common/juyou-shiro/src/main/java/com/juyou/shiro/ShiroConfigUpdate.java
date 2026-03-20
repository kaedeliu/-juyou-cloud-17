package com.juyou.shiro;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class ShiroConfigUpdate {

	@Autowired
    private EnvUtils envUtils;

	@Autowired
	ShiroFilterFactoryBean shiroFilterFactoryBean;
	
	public boolean updatePermission() {
        boolean flag = false;
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
                PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
                DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
                // 1. 清空老的权限控制
                manager.getFilterChains().clear();
                shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

             // 拦截器
                Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
                String excludeUrls = envUtils.value(EnvKey.SHIRO放行);
                if(!StringUtils.isEmpty(excludeUrls)){
                    String[] permissionUrl = excludeUrls.split(",");
                    for(String url : permissionUrl){
                        filterChainDefinitionMap.put(url , "anon");
                    }
                }

                // Swagger/Knife4j 放行（避免聚合文档 302/跳转）
                String[] swaggerAnonUrls = new String[]{
                        "/v2/api-docs",
                        "/v2/api-docs/**",
                        "/v3/api-docs",
                        "/v3/api-docs/**",
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
                //        如果cloudServer为空 则说明是单体 需要加载跨域配置
                Object cloudServer = envUtils.env.getProperty(CommonConstant.CLOUD_SERVER_KEY);
                filterMap.put("jwt", new JwtFilter(cloudServer==null));
                shiroFilterFactoryBean.setFilters(filterMap);

                filterChainDefinitionMap.put("/**", "jwt");
                
                // 未授权界面返回JSON
                shiroFilterFactoryBean.setUnauthorizedUrl("/sys/common/403");
                shiroFilterFactoryBean.setLoginUrl("/sys/common/noLogin");
                
                shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
                // 3. 重新构建生成
                Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
                for (Map.Entry<String, String> entry : chains.entrySet()) {
                    String url = entry.getKey();
                    String chainDefinition = entry.getValue().trim().replace(" ", "");
                    manager.createChain(url, chainDefinition);
                }
                log.info("更新权限成功");
            } catch (Exception e) {
            	log.error("更新权限失败",e);
                throw new RuntimeException("更新shiro权限出现错误!");
            }
        }
        return flag;
    }
}
