//package com.juyou.common.gateway.error;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ErrorProperties;
//import org.springframework.boot.autoconfigure.web.WebProperties;
//import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
//import org.springframework.boot.web.reactive.error.ErrorAttributes;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.BodyInserters;
//import org.springframework.web.reactive.function.server.*;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//public class CustomExceptionHandler extends DefaultErrorWebExceptionHandler {
//
//
//	@Autowired
//	GateWayExceptionHandlerAdvice gateWayExceptionHandlerAdvice;
//
//    public CustomExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resourceProperties,
//			ErrorProperties errorProperties, ApplicationContext applicationContext) {
//		super(errorAttributes, resourceProperties, errorProperties, applicationContext);
//		// TODO Auto-generated constructor stub
//	}
//
//
////        /**
////         * 获取异常属性
////         */
////        @Override
////        protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
////            int code = HttpStatus.INTERNAL_SERVER_ERROR.value();
////            Throwable error = super.getError(request);
////            if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {
////                code = HttpStatus.NOT_FOUND.value();
////            }
////            return response(code, this.buildMessage(request, error));
////        }
//
//        /**
//         * 指定响应处理方法为JSON处理的方法
//         * @param errorAttributes
//         */
//        @Override
//        protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
//            return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
//        }
//
//
//        /**
//         * 根据code获取对应的HttpStatus
//         * @param errorAttributes
//         */
//        @Override
//        protected int getHttpStatus(Map<String, Object> errorAttributes) {
////            int statusCode = (int) errorAttributes.get("status");
//            return 200;
//        }
//
//        /**
//         * 重写 异常方法 塞入自己的 gateWayExceptionHandlerAdvice
//         *
//         * @param request
//         * @return
//         */
//        @SuppressWarnings("deprecation")
//		@Override
//        protected Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
//            boolean includeStackTrace = isIncludeStackTrace(request, MediaType.ALL);
//            Map<String, Object> error = getErrorAttributes(request, includeStackTrace);
//            Throwable throwable = getError(request);
//            int status=super.getHttpStatus(error);
//            try {
//            	//处理下聚合swagger返回数据的问题
//            	String uri=request.exchange().getRequest().getURI().getPath();
//            	if(uri.endsWith("/v2/api-docs"))
//            		status=200;
//			} catch (Exception e) {
//			}
//
//            return ServerResponse.status(status)
//                    .contentType(MediaType.APPLICATION_JSON_UTF8)
//                    .body(BodyInserters.fromObject(gateWayExceptionHandlerAdvice.handle(throwable)));
//
//        }
//
////        /**
////         * 构建异常信息
////         * @param request
////         * @param ex
////         * @return
////         */
////        private String buildMessage(ServerRequest request, Throwable ex) {
////            StringBuilder message = new StringBuilder("Failed to handle request [");
////            message.append(request.methodName());
////            message.append(" ");
////            message.append(request.uri());
////            message.append("]");
////            if (ex != null) {
////                message.append(": ");
////                message.append(ex.getMessage());
////            }
////            return message.toString();
////        }
////
////        /**
////         * 构建返回的JSON数据格式
////         * @param status        状态码
////         * @param errorMessage  异常信息
////         * @return
////         */
////        public static Map<String, Object> response(int status, String errorMessage) {
////            Map<String, Object> map = new HashMap<>();
////            map.put("code", status);
////            map.put("message", errorMessage);
////            map.put("data", null);
////            log.error(map.toString());
////            return map;
////        }
//}
