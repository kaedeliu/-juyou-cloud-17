package com.juyou.security;


import com.juyou.common.constant.CommonConstant;
import com.juyou.security.constant.SecurityConstant;
import com.juyou.security.sevice.SecurityService;
import com.juyou.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

/**
 * @Author kaedeliu
 * @description 验证用户权限
 */
@Component
@Slf4j
public class DefaultAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    SecurityService securityService;

    @Autowired
    SecurityUtils securityUtils;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        log.info("----------DefaultAuthorizationManager check-----------"+authorizationContext.getExchange().getRequest().getURI());

        return authentication.map(auth -> {
            if(SecurityConstant.ANON_NAME.equals(auth.getName())){
                return new AuthorizationDecision(true); //白名单直接通过
            }
            ServerWebExchange exchange = authorizationContext.getExchange();
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();
            Collection<GrantedAuthority> grantedAuthoritys = (Collection<GrantedAuthority>) auth.getAuthorities();
            List<String> roleCodes=grantedAuthoritys.stream().map(GrantedAuthority::getAuthority).toList();

            if(roleCodes.contains(CommonConstant.SYS_ADMIN)){ //系统超管，直接过
                return new AuthorizationDecision(true);
            }

            //先处理角色白名单
            List<String> whilePaths=securityUtils.getRoleWhiltPaths(grantedAuthoritys);
            if(whilePaths!=null && !whilePaths.isEmpty()){
                for (String authority : whilePaths) {
                    if (antPathMatcher.match(authority, path)) {
                        log.debug(String.format("角色白名单，GrantedAuthority:{%s}  Path:{%s} ", authority, path));
                        return new AuthorizationDecision(true);
                    }
                }
            }

            List<String> authorities=securityService.getUserAuthority(auth);

            for (String authority : authorities) {
                // TODO
                // 查询用户访问所需角色进行对比
                if (antPathMatcher.match(authority, path)) {
                    log.debug(String.format("用户请求API校验通过，GrantedAuthority:{%s}  Path:{%s} ", authority, path));
                    return new AuthorizationDecision(true);
                }
            }
            log.info("没有权限:"+authorizationContext.getExchange().getRequest().getURI());
            return new AuthorizationDecision(false);
        }).defaultIfEmpty(new AuthorizationDecision(false));
    }

//    @Override
//    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
//        log.info("----------DefaultAuthorizationManager verify-----------");
//        return check(authentication, object)
//                .filter(AuthorizationDecision::isGranted)
//                .switchIfEmpty(Mono.defer(() -> {
//                    String body = "{}";
//                    return Mono.error(new AccessDeniedException(body));
//                }))
//                .flatMap(d -> Mono.empty());
//    }
}
