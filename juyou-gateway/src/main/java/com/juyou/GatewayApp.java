package com.juyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;


@SpringBootApplication(exclude ={DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableCaching
@EnableFeignClients
public class GatewayApp 
{
    void setDefaultTimezone() {
     TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    } 
	
    public static void main( String[] args )
    {
    	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    	SpringApplication.run(GatewayApp.class, args);
    }
}
