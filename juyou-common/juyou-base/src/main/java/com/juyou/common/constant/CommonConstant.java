package com.juyou.common.constant;

/**
 * 系统常量定义
 * @author Administrator
 *
 */
public interface CommonConstant {

	/**
	 * 图片验证码缓存
	 */
	public static final String SYS_CACHE_IMGCODE = "sys:cache:imgcode";


	/**
	 * 所有数据权限
	 */
	public static final String SYS_CACHE_PERMISSION = "sys:cache:Permission";

	public static final String SYS_CACHE_ROLE_PERMISSION = "sys:cache:role:permission";

	/**
	 * 主键KEY
	 */
	public static final String SYS_CACHE_PK = "sys:cache:pk";


	public final static String X_ACCESS_TOKEN = "X-Access-Token";


	/**
	 * 字段缓存key
	 */
	public static final String SYS_CACHE_DICT = "sys:cache:dict";

	/**
	 * 字典在系统配置中的key
	 */
	public static final String SYS_CACHE_DICT_CONFIG = "dictVersion";

	public static final String SYS_CACHE_CONFIG = "sys:cache:config";

	/**
	 * 用户登录的KEY
	 */
	public static final String SYS_LOGIN_USER = "sys:login:user";

	/**
	 * 角色权限
	 */
	public static final String SYS_ROLE_AUTH = "sys:role:auth";

	/**
	 * 租户信息
	 */
	public static final String SYS_TENANTS = "sys:tenants";

	/**
	 * 超级管理员的角色code
	 */
	public static final String SYS_ADMIN = "sysAdmin";


	/**
	 * 租户管理员权限
	 */
	public static final String SYS_TENANTS_ADMIN_AUTH = "sysy:tenants:admin:auth";

	/**
	 * 租户管理员
	 */
	public static final String TENANTS_ADMIN = "tenantsAdmin";


	/**
	 * 系统日志类型： 登录
	 */
	public static final int LOG_登录 = 1;

	/**
	 * 系统日志类型： 操作
	 */
	public static final int LOG_操作 = 2;

	/**
	 * 操作日志类型： 查询
	 */
	public static final int OPERATE_查询_1 = 1;

	/**
	 * 操作日志类型： 添加
	 */
	public static final int OPERATE_添加_2 = 2;

	/**
	 * 操作日志类型： 更新
	 */
	public static final int OPERATE_更新_3 = 3;

	/**
	 * 操作日志类型： 删除
	 */
	public static final int OPERATE_删除_4 = 4;

	/**
	 * 操作日志类型： 导入
	 */
	public static final int OPERATE_导入_5 = 5;

	/**
	 * 操作日志类型： 导出
	 */
	public static final int OPERATE_导出_6 = 6;

	/**字典翻译文本后缀*/
	public static final String DICT_TEXT_SUFFIX = "_dictText";



	/**
	 * 常规错误
	 */
	public static final String COMMON_ERROR_CODE="A9001";

	/**
	 * 未登录编号
	 */
	public static final String NO_LOGIN="A9999";

	/**
	 * redis缓存间隔符号
	 */
	public static final String REDIS_BUFF="::";


	/**
	 * 文章分类缓存
	 */
	public static final String SYS_INFO_TYPE = "sys:info:type";

	/**
	 * 文章分类属性缓存
	 */

	public static final String SYS_INFO_TYPE_ATTR = "sys:info:type:attr";
}

