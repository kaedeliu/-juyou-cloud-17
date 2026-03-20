package com.juyou.common.config;//package com.juyou.common.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * @功能 开启跨域请求
// * @Author kaedeliu
// * @创建时间 2026/3/18 10:49
// * @修改人 kaedeliu
// * @修改时间 2026/3/18 10:49
// * @Param
// * @return
//**/
//@Configuration
//public class CrosConfig {
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/*")
//                        .allowedOrigins("*")
//                        .allowCredentials(true)
//                        .allowedMethods("GET", "POST", "DELETE", "PUT","PATCH")
//                        .maxAge(3600);
//            }
//        };
//    }
//}
