package com.juyou.common.json;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.serializer.PropertyFilter;


/**
 * JSON格式化过滤器
 * @author kaedeliu
 *
 */
public class JsonFilter  implements PropertyFilter{
	
	private Map<Class<?>, Set<String>> includeMap = new HashMap<Class<?>, Set<String>>();
	
	private Map<Class<?>, Set<String>> filterMap = new HashMap<Class<?>, Set<String>>();
	
	public boolean apply(Object source, String name, Object value) {
		if(includeMap.containsKey(source.getClass())) {
			Set<String> fields = includeMap.get(source.getClass());
			return fields.contains(name);
		}
		if(filterMap.containsKey(source.getClass())) {
			Set<String> fields = filterMap.get(source.getClass());
			return !fields.contains(name);
		}
		return true;
	}
	public void include(Class<?> clazz,String... name){
		Set<String> sets = new HashSet<String>();
		for (String string : name) {
			sets.add(string);
		}
		includeMap.put(clazz, sets);
	}
	
	public void filter(Class<?> clazz,String... name){
		Set<String> sets = new HashSet<String>();
		for (String string : name) {
			sets.add(string);
		}
		filterMap.put(clazz, sets);
	}
	
	public void addJSON(JSON json) {
		if(StringUtils.hasLength(json.filter())) {
			filter(json.type(), json.filter().split(","));
		}else if(StringUtils.hasLength(json.include())) {
			include(json.type(), json.include().split(","));
		}
	}
	
	public JsonFilter(){}
	
	public JsonFilter(Map<Class<?>, Set<String>> includeMap){
		this.includeMap = includeMap;
	}
}
