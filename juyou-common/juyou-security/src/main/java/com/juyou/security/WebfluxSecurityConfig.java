package com.juyou.security;

import com.juyou.security.config.SecurityProConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.annotation.Resource;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 10:56
 * @description webflux security核心配置类
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@Slf4j
public class WebfluxSecurityConfig {

    @Resource
    DefaultAuthorizationManager defaultAuthorizationManager;

    /**
     * 处理token验证，如果通过直接不走用户信息验证
     * 注意名字注入，双方需要名字匹配
     */
//    @Resource(name="tokenAuthenticationManager")
//    private ReactiveAuthenticationManager tokenAuthenticationManager;

//    /**
//     * 由user项目去实现登录验证，此项目自身不实现此接口,后续引入项目处理此接口的实现
//     * 注意名字注入，需要双方名字匹配
//     */
//    @Resource(name = "userDetailsServiceImpl")
//    private ReactiveUserDetailsService userDetailsServiceImpl;

    /**
     * 验证密码、处理权限角色的数据
     * 注意名字注入，双方需要名字匹配
     */
//    @Resource(name="loginAuthenticationManager")
//    private ReactiveAuthenticationManager loginAuthenticationManager;


//    @Resource
//    private DefaultAuthenticationSuccessHandler defaultAuthenticationSuccessHandler;
//
//    @Resource
//    private DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler;

    @Resource
    DefaultSecurityContextRepository defaultSecurityContextRepository;

    @Resource
    DefaultAuthenticationEntryPoint defaultAuthenticationEntryPoint;

    @Resource
    DefaultAccessDeniedHandler defaultAccessDeniedHandler;

    @Autowired
    SecurityProConfig securityProConfig;


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        // Swagger/Knife4j 需要匿名可访问，否则聚合分组请求 api-docs 会被重定向(302)/拦截导致 Swagger 不可用
        String[] pathMatchers = new String[]{
                "/favicon1.ico",
                "/**/v3/api-docs",
                "/**/v3/api-docs/**",
                "/**/v2/api-docs",
                "/**/v2/api-docs/**",
                "/**/doc.html",
                "/**/swagger-ui.html",
                "/**/swagger-ui/**",
                "/**/webjars/**",
                "/swagger-resources",
                "/**/swagger-resources/**",
                "/swagger-resources/configuration/ui",
                "/swagger-resources/configuration/security",
                "/configuration/ui",
                "/configuration/security",
                "/**/configuration/**",
                "/**/knife4j/**"
        };

        httpSecurity
                // 登录认证处理
//                .authenticationManager(tokenAuthenticationManager)
                .securityContextRepository(defaultSecurityContextRepository)
                // 请求拦截处理
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(pathMatchers).permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().access(defaultAuthorizationManager)

                )

                .formLogin().loginPage("/loginxxsssaaa---")


                // 禁用session ,Webflux session默认立即过去，所以不用配置
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 自定义处理
//                .authenticationSuccessHandler(defaultAuthenticationSuccessHandler)  //登录成功
//                .authenticationFailureHandler(defaultAuthenticationFailureHandler)  //登录失败
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(defaultAuthenticationEntryPoint)   //未认证
                .accessDeniedHandler(defaultAccessDeniedHandler)   //权限认证
//                .and()
//                .addFilterAfter()
//                .and()
//                .exceptionHandling()

                .and()
                .csrf().disable()

        ;
        return httpSecurity.build();

    }

    /**
     * BCrypt密码编码
     */
//    @Bean("passwordEncoder")
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }

    /**
     * 注册用户信息验证管理器，可按需求添加多个按顺序执行
     */
//    @Bean
//    ReactiveAuthenticationManager reactiveAuthenticationManager() {
//        log.info("------ reactiveAuthenticationManager -------");
//        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
//        managers.add(authentication -> {
//            // 其他登陆方式 (比如手机号验证码登陆) 可在此设置不得抛出异常或者 Mono.error
//            return Mono.empty();
//        });
//
//        // 必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此 AuthenticationManager 会调用 Mono.error 造成后面的 AuthenticationManager 不生效
//        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsServiceImpl));
////        managers.add(tokenAuthenticationManager);
//        return new DelegatingReactiveAuthenticationManager(managers);
//    }

}
