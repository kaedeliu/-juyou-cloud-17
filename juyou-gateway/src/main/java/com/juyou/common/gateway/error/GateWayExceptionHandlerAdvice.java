package com.juyou.common.gateway.error;

import com.juyou.common.error.ErrorCode;
import com.juyou.common.error.ErrorMsgUtil;
import com.juyou.common.exception.BaseException;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.security.SignatureException;

@Component
@Slf4j
public class GateWayExceptionHandlerAdvice {

	@Autowired
	ErrorMsgUtil errorMsgUtil;
	/**
     * 统一 异常
     *
     * @param throwable
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public String handle(Throwable throwable) {

        if (throwable instanceof SignatureException) {
            return signHandle((SignatureException) throwable);
        } else if (throwable instanceof NotFoundException) {
            return notFoundHandle((NotFoundException) throwable);
        } else if (throwable instanceof ResponseStatusException) {
            return handle((ResponseStatusException) throwable);
//        } else if (throwable instanceof GateWayException) {
//            return badGatewayHandle((GateWayException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            return timeoutHandle((ConnectTimeoutException) throwable);
        }
//        else if(throwable instanceof HystrixRuntimeException){
//        	return hystrixRuntimeExceptionHandle((HystrixRuntimeException) throwable);
//        }
        else if(throwable instanceof BaseException){
        	return baseExceptionHandle((BaseException) throwable);
        }
        else {
            log.error("e:"+throwable);
            return errorMsgUtil.result(null).toJsonString();
        }
    }
    
    /**
     * 401 校验 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {BaseException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String baseExceptionHandle(BaseException ex) {
        log.error("baseException:{}", ex.getMessage());
        return errorMsgUtil.result(ex.getCode(),ex.getMessage()).toJsonString();
//        return CommonResult.failed(ResultCode.UNAUTHORIZED);
    }
    
//    @ExceptionHandler(value = {HystrixRuntimeException.class})
//    public String hystrixRuntimeExceptionHandle(HystrixRuntimeException ex) {
//    	if(ex.getCause() instanceof BaseException) {
//    		return baseExceptionHandle((BaseException) ex.getCause());
//    	}
//    	 return errorMsgUtil.result(ErrorCode.A9300).toJsonString();
//    }

    /**
     * 401 校验 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {SignatureException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String signHandle(SignatureException ex) {
        log.error("SignatureException", ex);
        return errorMsgUtil.result(ErrorCode.A9102).toJsonString();
//        return CommonResult.failed(ResultCode.UNAUTHORIZED);
    }

    /**
     * 404 服务未找到 异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {NotFoundException.class})
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String notFoundHandle(NotFoundException ex) {
        log.error("not found exception", ex);
        return errorMsgUtil.result(ErrorCode.A9301,"服务访问失败").toJsonString();
    }


    /**
     * 500 其他服务异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ResponseStatusException.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(ResponseStatusException ex) {
    	if(ex.getStatus().value()==404){

        }
        log.error("ResponseStatusException", ex);
        return errorMsgUtil.result(ErrorCode.A9301,"服务访问异常").toJsonString();
    }

//    /**
//     * 502 错误网关 异常
//     *
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(value = {GateWayException.class})
////    @ResponseStatus(HttpStatus.BAD_GATEWAY)
//    public String badGatewayHandle(GateWayException ex) {
//        log.error("badGateway exception:{}", ex.getMessage());
//        return CommonResult.failed(ResultCode.BAD_GATEWAY);
//    }


    /**
     * 504 网关超时异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ConnectTimeoutException.class})
//    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public String timeoutHandle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:", ex);
        return errorMsgUtil.result(ErrorCode.A9300,"请求超时").toJsonString();
    }
}
