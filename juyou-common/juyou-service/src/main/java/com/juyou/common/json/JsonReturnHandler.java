package com.juyou.common.json;

import com.juyou.common.utils.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

 
 /**
  *    自定义JSON过滤器，controller函数上使用
  * 示例1:
  * @JSON(type=XXXX.class,filter="字段1,字段2",include="字段1,字段2")
  * @JSON(type=XXXX.class,filter="字段1,字段2",include="字段1,字段2")
  * 示例2::
  * @JSONS({
  * @JSON(type=XXXX.class,filter="字段1,字段2",include="字段1,字段2")
  * @JSON(type=XXXX.class,filter="字段1,字段2",include="字段1,字段2")
  * })
  * @author 27174
  *
  */
@Component
public class JsonReturnHandler implements HandlerMethodReturnValueHandler{

	
    @Override
    public boolean supportsReturnType(MethodParameter returnType) {  
        // 如果有我们自定义的 JSON 注解 就用我们这个Handler 来处理

       return returnType.hasMethodAnnotation(JSON.class) || returnType.getParameterType() == Result.class;
//    	return false;
    }
 
    @SuppressWarnings("deprecation")
	@Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest) throws Exception {
    	mavContainer.setRequestHandled(true);
    	HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
    	response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    	JsonFilter jsonFilter=null;
    	if(returnType.hasMethodAnnotation(JSON.class)) {
    		 Annotation[] annos = returnType.getMethodAnnotations();
    		 jsonFilter=new JsonFilter();
    		 for (Annotation a : annos) {
    			 if (a instanceof JSON) {
   	               JSON json = (JSON) a;
   	               jsonFilter.addJSON(json);
   	            } else if (a instanceof JSONS) { // 使用多重注解时，实际返回的是 @Repeatable(JSONS.class) 内指定的 @JSONS 注解
   	                JSONS jsons = (JSONS) a;
   	                for (JSON json : jsons.value()) {
   	                	jsonFilter.addJSON(json);
   	                }
   	            }
    		 }
    	}else {
    		if(returnValue.getClass()==Result.class) {
    			Result<?> result=(Result<?>) returnValue;
    			if(result.getFilter()!=null && result.getFilter().getClass() ==  JsonFilter.class) {
    				jsonFilter =(JsonFilter) result.getFilter();
    			}
    		}
    	}
    	String json=null;
    	if(StringUtils.hasLength(FJsonConfig.getConfig().getDateFormat())){
			com.alibaba.fastjson.JSON.DEFFAULT_DATE_FORMAT=FJsonConfig.getConfig().getDateFormat();
		}
    	if(jsonFilter!=null) {
    		json=com.alibaba.fastjson.JSON.toJSONString(returnValue, FJsonConfig.getConfig().getSerializeConfig(),jsonFilter,FJsonConfig.getConfig().getSerializerFeatures());
    	}else {
    		json=com.alibaba.fastjson.JSON.toJSONString(returnValue, FJsonConfig.getConfig().getSerializeConfig(),FJsonConfig.getConfig().getSerializerFeatures());
    	}
    	response.getWriter().write(json);
//    	if(returnType.hasMethodAnnotation(JSON.class)) {
//	        // 设置这个就是最终的处理类了，处理完不再去找下一个类进行处理
//	        mavContainer.setRequestHandled(true);
//	        
//	        // 获得注解并执行filter方法 最后返回
//	        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//	        Annotation[] annos = returnType.getMethodAnnotations();
//	        CustomerJsonSerializer jsonSerializer = new CustomerJsonSerializer();
//	        Arrays.asList(annos).forEach(a -> { // 解析注解，设置过滤条件
//	            if (a instanceof JSON) {
//	                JSON json = (JSON) a;
//	                jsonSerializer.filter(json);
//	            } else if (a instanceof JSONS) { // 使用多重注解时，实际返回的是 @Repeatable(JSONS.class) 内指定的 @JSONS 注解
//	                JSONS jsons = (JSONS) a;
//	                Arrays.asList(jsons.value()).forEach(json -> {
//	                    jsonSerializer.filter(json);
//	                });
//	            }
//	        });
//	        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//	        String json = jsonSerializer.toJson(returnValue);
//	        response.getWriter().write(json);
//    	}else {
//    		if(returnValue.getClass()==Result.class) {
//    			Result<?> result=(Result<?>) returnValue;
//    			if(result.getFilter()!=null && result.getFilter().getClass() ==  JsonFilter.class) {
//    				HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//    				response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
//    		        String json = com.alibaba.fastjson.JSON.toJSONString(result,(SerializeFilter) result.getFilter());
//    		        response.getWriter().write(json);
//    		        response.flushBuffer();
//    			}
//    		}
//    	}
    }
}