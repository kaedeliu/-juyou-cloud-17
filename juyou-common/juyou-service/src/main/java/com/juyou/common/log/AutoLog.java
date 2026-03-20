package com.juyou.common.log;



import java.lang.annotation.*;


/**
 * 系统日志注解
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoLog {

	/**
	 * 日志内容
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 日志类型
	 * 
	 * @return 1:登录日志;2:操作日志;
	 */
	int logType() default 2;
	
	/**
	 * 操作日志类型
	 * 
	 * @return （1查询，2添加，3修改，4删除）
	 */
	int operateType() default 0;
	
	/**
	 * 是否保存数据库
	 * @return
	 */
	boolean db() default true;

	/**
	 * 是否忽略
	 * @return
	 */
	boolean ignore() default false;

	/**
	 * 是否打印出参
	 * @return
	 */
	boolean response() default false;
}
