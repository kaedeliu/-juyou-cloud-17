package com.juyou.common.gateway.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @功能 全局异常处理
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {

    @Autowired
    GateWayExceptionHandlerAdvice gateWayExceptionHandlerAdvice;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        String url=exchange.getRequest().getURI().getPath();
        String msg=null;
        if(url!=null && ("/health".equals(url) || url.endsWith("/favicon.ico") || url.endsWith("/def-api-docs"))){ //阿里云SLB健康检测
            msg="{}";
        }else{
            msg= gateWayExceptionHandlerAdvice.handle(ex);
        }

        ServerHttpResponse response = exchange.getResponse();
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        //ex.printStackTrace();
        log.error("url:"+url+" msg:"+msg+"  "+ex.getMessage());
        String finalMsg = msg;
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            //todo 返回响应结果，根据业务需求，自己定制
            return bufferFactory.wrap(finalMsg.getBytes(StandardCharsets.UTF_8));
        }));

    }
}
