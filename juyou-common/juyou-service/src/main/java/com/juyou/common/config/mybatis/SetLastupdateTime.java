package com.juyou.common.config.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否强制设置更新时间
 * @author kaedeliu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Documented
public @interface SetLastupdateTime {

}
