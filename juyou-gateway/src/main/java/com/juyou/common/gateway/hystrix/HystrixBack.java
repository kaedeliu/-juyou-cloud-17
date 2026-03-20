package com.juyou.common.gateway.hystrix;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

@RestController
@Slf4j
public class HystrixBack {

	@GetMapping("/fallback")
    public String fallback(ServerWebExchange exchange, Throwable throwable) throws Throwable{
        log.info("44444");
//			Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
//		Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_);
//	     	ServerWebExchange delegate = ((ServerWebExchangeDecorator) exchange).getDelegate();
//	     	log.error("接口调用失败，URL={} error:{}", delegate.getRequest().getURI(), exception.getMessage());
	     	throw throwable;
//	        if (exception instanceof HystrixTimeoutException) {
//	        	return Result.error("接口调用超时").toJsonString();
//	            //result.put("msg", "接口调用超时");
//	        } else if (exception != null && exception.getMessage() != null) {
//	        	return Result.error("接口调用超时").toJsonString();
//	           // result.put("msg", "接口调用失败: " + exception.getMessage());
//	        } else {
//	        	return Result.error("接口调用超时").toJsonString();
//	        }
	     
    }
}
