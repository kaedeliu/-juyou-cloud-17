package com.juyou.common.json;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * 切面处理JSON过滤问题
 * @author 27174
 *
 */
@Configuration
public class JsonAdvice implements InitializingBean {

    @Autowired
    private RequestMappingHandlerAdapter adapter;
    
    @Autowired
    JsonReturnHandler  jsonReturnHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> handlers = new ArrayList<HandlerMethodReturnValueHandler>(returnValueHandlers);
        this.decorateHandlers(handlers);
        adapter.setReturnValueHandlers(handlers);
    }


    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                int index = handlers.indexOf(handler);
                handlers.add(index, jsonReturnHandler);
                break;
            }
        }
    }
}
