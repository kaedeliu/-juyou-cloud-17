package com.juyou;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScans;

import java.util.TimeZone;
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages ={"com.juyou.*.*.mapper,com.juyou.*.mapper"})
public class ArticleApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {

        //设置时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(ArticleApplication.class, args);
    }
}