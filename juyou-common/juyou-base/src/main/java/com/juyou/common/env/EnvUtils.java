package com.juyou.common.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 获取配置
 * @author kaedeliu
 *
 */
@Component
public class EnvUtils {
	
	@Autowired
    public Environment env;
	
	/**
	 * 获取配置,当值为空是返回默认值
	 * @param envKey
	 * @return
	 */
	public String value(EnvKey envKey) {
		String key=envKey.key();
		String value=env.getProperty(key);
		if(StringUtils.isEmpty(value))
			return (String) envKey.defaultValue;
		return value;
	}
	
	/**
	 * 获取配置,当值为空是返回默认值
	 * @param <T>
	 * @param envKey
	 * @param targetType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T value(EnvKey envKey,Class<T> targetType) {
		String key=envKey.key();
		T value=env.getProperty(key, targetType);
		if(StringUtils.isEmpty(value))
			return  (T) envKey.defaultValue;
		return value;
		
	}
}
