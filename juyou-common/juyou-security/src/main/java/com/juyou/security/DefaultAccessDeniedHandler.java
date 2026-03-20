package com.juyou.security;

import com.alibaba.fastjson.JSONObject;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * @author ShiLei
 * @version 1.0.0
 * @date 2021/3/11 11:12
 * @description 鉴权管理失败
 */
@Component
@Slf4j
public class DefaultAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        log.info("-------- DefaultAccessDeniedHandler------");
        return Mono.defer(() -> Mono.just(exchange.getResponse()))
                .flatMap(response -> {
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    DataBufferFactory dataBufferFactory = response.bufferFactory();
                    String result = JSONObject.toJSONString(Result.error(CommonConstant.COMMON_ERROR_CODE,"权限验证失败:"+ exchange.getRequest().getURI().getPath()));
                    DataBuffer buffer = dataBufferFactory.wrap(result.getBytes(
                            Charset.defaultCharset()));
                    return response.writeWith(Mono.just(buffer));
                });
    }
}
