package com.juyou.common.json;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.juyou.common.config.ByteArrayOpenApiFastJson;
import com.juyou.common.config.StringOpenApiFastJson;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 将默认的转换器配置为fastJSON
 * @author kaedeliu
 *
 */
@Configuration
public class FJsonConfig {

    static final  FastJsonConfig  config=null;

	@Bean
    public HttpMessageConverter<Object> configureMessageConverters() {
		
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = getConfig();
        converter.setFastJsonConfig(config);
        //fastjson序列化导致OpenApi3 JSON格式错误 使用此处理掉
        config.getSerializeConfig().put(String.class, StringOpenApiFastJson.instance);
        config.getSerializeConfig().put(byte[].class, ByteArrayOpenApiFastJson.instance);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> mediaTypeList = new ArrayList<>();
        // 解决中文乱码问题，相当于在Controller上的@RequestMapping中加了个属性produces = "application/json"
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(mediaTypeList);
        return converter;
    }

    public static FastJsonConfig getConfig(){
        if(config!=null) return config;

        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(
                // 保留map空的字段
//                SerializerFeature.WriteMapNullValue,
                // 将String类型的null转成""
//                SerializerFeature.WriteNullStringAsEmpty,
                // 将Number类型的null转成0
//                SerializerFeature.WriteNullNumberAsZero,
                // 将List类型的null转成[]
//                SerializerFeature.WriteNullListAsEmpty,
                // 将Boolean类型的null转成false
//                SerializerFeature.WriteNullBooleanAsFalse,
                //统一时间格式
                SerializerFeature.WriteDateUseDateFormat,
                // 避免循环引用
                SerializerFeature.DisableCircularReferenceDetect);
        //全局指定了日期格式
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return config;
    }
}
