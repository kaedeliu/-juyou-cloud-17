package com.juyou.common.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @功能 用于标明需要自动创建token，用于通讯
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreareToken {
}
