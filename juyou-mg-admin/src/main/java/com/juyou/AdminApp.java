package com.juyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class AdminApp {

	@PostConstruct
	void setDefaultTimezone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public static void main(String[] args) {
		// 设置时区
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
		SpringApplication.run(AdminApp.class, args);
	}
}
