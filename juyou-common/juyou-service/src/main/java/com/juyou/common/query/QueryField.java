package com.juyou.common.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询用于注解别名
 * @author 27174
 *
 */
@Target({ElementType.LOCAL_VARIABLE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface  QueryField {

	String value();
}
