package com.juyou.common.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class KeyResolverConfig {

	@Bean(value = "KeyResolverApi")
    public KeyResolver KeyResolverApi() {
        // 根据ip限流
//        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
        // 根据接口限流
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
        // 根据用户限流
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }
}
