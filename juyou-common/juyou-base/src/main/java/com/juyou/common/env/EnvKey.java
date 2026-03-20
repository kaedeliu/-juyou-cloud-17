package com.juyou.common.env;


/**
 * 统一定义配置文件KEY常量,便于刷新以及维护
 * @author kaedeliu
 *
 */
	public enum EnvKey {


	系统登录超时时间("cdyx.sys.login.time.system",86400000L),


	任务调度类型("cdyx.task.type",null),

	任务调度状态("cdyx.task.enable",false),

	自动设置DB数据("cdyx.db.set",true),

	是否使用前置机转发("cdyx.interface.network.frontComputer",false),
	前置机转发地址("cdyx.interface.network.frontComputerForward",null),

	
	登录Secrets("cdyx.security.jwtSecret","jwtSecret%$&1"),
	客户端登录Secrets("cdyx.login.clientsecrets","x3d1)$#4xa"),
	客户端登录feign("cdyx.login.feign","x3d2)$#4xa"),

	日志_异常日志类型("logging.exception.type",1),
	日志_请求日志类型("logging.request.type",1),

	导出文件存放地址("cdyx.export.path",null),

	日志是否保存数据库("logging.dbsave",true),

	/**
	 * SpringDoc 文档访问地址（可配置多个，逗号分隔）
	 */
	SpringDoc请求地址("springdoc.server.urls", null),

	;

	



	String key;

	Object defaultValue;

	EnvKey(String key,Object defaultValue) {
		this.key=key;
		this.defaultValue=defaultValue;
	}

	public String key() {
		return this.key;
	}
}
