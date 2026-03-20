package com.juyou.common.json;

//import java.text.SimpleDateFormat;
//
//import org.apache.commons.lang3.StringUtils;
//
//import com.fasterxml.jackson.annotation.JsonInclude.Include;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 使用JsonFiter
 */
@Deprecated
public class CustomerJsonSerializer {

//	ObjectMapper mapper = new ObjectMapper();
//    JacksonJsonFilter jacksonFilter = new JacksonJsonFilter();
//
//    public CustomerJsonSerializer() {
//    	mapper.setSerializationInclusion(Include.NON_NULL);
//
//    	mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//
//    	//取消时间的转化格式,默认是时间戳,可以取消,同时需要设置要表现的时间格式
//    	mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    	mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//    }
//    /**
//     * @param clazz target type
//     * @param include include fields
//     * @param filter filter fields
//     */
//    public void filter(Class<?> clazz, String include, String filter) {
//        if (clazz == null) return;
//        if (StringUtils.isNotBlank(include)) {
//            jacksonFilter.include(clazz, include.split(","));
//        }
//        if (StringUtils.isNotBlank(filter)) {
//            jacksonFilter.filter(clazz, filter.split(","));
//        }
//        mapper.addMixIn(clazz, jacksonFilter.getClass());
//    }
//
//    public String toJson(Object object) throws JsonProcessingException {
//
//        mapper.setFilterProvider(jacksonFilter);
//        return mapper.writeValueAsString(object);
//    }
//
//    public void filter(JSON json) {
//        this.filter(json.type(), json.include(), json.filter());
//    }
//
//    public void setFilter(JacksonJsonFilter jacksonFilter) {
//    	this.jacksonFilter=jacksonFilter;
//    }
}
