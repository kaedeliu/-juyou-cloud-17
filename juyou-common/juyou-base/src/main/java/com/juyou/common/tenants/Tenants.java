package com.juyou.common.tenants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多租户处理，通常只处理列表
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Tenants {

    /**
     * 多租户级联查询时的字段标识
     * @return
     */
    String value() default "tenants_id";
}
