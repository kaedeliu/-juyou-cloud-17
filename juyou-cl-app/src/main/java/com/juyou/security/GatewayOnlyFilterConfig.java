package com.juyou.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class GatewayOnlyFilterConfig {

    @Bean
    public FilterRegistrationBean<GatewayOnlyFilter> gatewayOnlyFilter() {
        FilterRegistrationBean<GatewayOnlyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new GatewayOnlyFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

