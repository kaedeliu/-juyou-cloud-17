package com.juyou.security;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.jwt.JwtUtil;
import com.juyou.security.config.SecurityProConfig;
import com.juyou.security.sevice.SecurityService;
import com.juyou.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author kaedeliu
 * @description 验证登录，存储登录类容
 */
@Component
@Slf4j
public class DefaultSecurityContextRepository implements ServerSecurityContextRepository {


    @Autowired
    SecurityUtils securityUtils;


    @Autowired
    SecurityProConfig securityProConfig;

    @Autowired
    SecurityService securityService;



    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request=exchange.getRequest();
        //log.info(" ---------- DefaultSecurityContextRepository -------");
        //允许匿名用户，直接给匿名用户，具体用户在token里面会有
        if(securityUtils.isWhilt(exchange.getRequest())){
            return Mono.just(securityUtils.getAnon()).map(SecurityContextImpl::new);
        }
        String token=request.getHeaders().getFirst(CommonConstant.X_ACCESS_TOKEN);
        if(StringUtils.isEmpty(token))
            return Mono.empty();
        //获取JWT的密钥
        String jwtSecret = securityProConfig.getJwtSecret();
        if(!JwtUtil.verify(token,jwtSecret)){
            return Mono.empty();
        }
        UsernamePasswordAuthenticationToken userToken= securityService.getUserAuthenticationToken(token);
        if(userToken!=null)
            return  Mono.just(userToken).map(SecurityContextImpl::new);
        return Mono.empty();
    }
}
