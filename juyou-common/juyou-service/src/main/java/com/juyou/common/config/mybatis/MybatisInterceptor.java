package com.juyou.common.config.mybatis;

import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.tenants.IgnorTenants;
import com.juyou.common.utils.oConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 * @Author kaedeliu
 * @Date  2019-01-19
 *
 */
@Slf4j
@Component
@Intercepts({
//		@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
		@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
		@Signature(type = Executor.class,method = "query",args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })//需要代理的对象和方法

})
public class MybatisInterceptor implements Interceptor {

	@Autowired
	LoginUtilsService loginUtilsService;

	@Autowired
	EnvUtils envUtils;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		String sqlId = mappedStatement.getId();
		log.debug("------sqlId------" + sqlId);
		SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
		Object parameter = invocation.getArgs()[1];
		log.debug("------sqlCommandType------" + sqlCommandType);
		long startTime = System.currentTimeMillis();
		try {
			if (parameter == null) {
				return invocation.proceed();
			}
			Boolean set=envUtils.value(EnvKey.自动设置DB数据,Boolean.class);
			if(set!=null && !set) {
				return invocation.proceed();
			}
//			if(SqlCommandType.SELECT == sqlCommandType){
//				Field[] fields = oConvertUtils.getAllFields(parameter);
//				log.error("1");
//			}
			if (SqlCommandType.INSERT == sqlCommandType) {
				LoginUserBasicDto sysUser = this.getLoginUser();
				Field[] fields = oConvertUtils.getAllFields(parameter);
				for (Field field : fields) {
					log.debug("------field.name------" + field.getName());
					try {
						if ("createName".equals(field.getName())) {
							field.setAccessible(true);
							Object local_createBy = field.get(parameter);
							field.setAccessible(false);
							if (local_createBy == null || local_createBy.equals("")) {
								if (sysUser != null) {
									// 登录人账号
									field.setAccessible(true);
									field.set(parameter, sysUser.getName());
									field.setAccessible(false);
								}
							}
						}
						// 注入创建时间
						//					if ("createTime".equals(field.getName())) {
						//						field.setAccessible(true);
						//						Object local_createDate = field.get(parameter);
						//						field.setAccessible(false);
						//						if (local_createDate == null || local_createDate.equals("")) {
						//							field.setAccessible(true);
						//							field.set(parameter, new Date());
						//							field.setAccessible(false);
						//						}
						//					}
						//					//注入租户编码
						if ("tenantsId".equals(field.getName()) ) {
							field.setAccessible(true);
							Object value=field.get(parameter);
							if (sysUser!=null  && (!field.isAnnotationPresent(IgnorTenants.class) && value==null)){
								// 获取登录用户信息
								field.setAccessible(true);
								field.set(parameter, sysUser.getTenantsId());
								field.setAccessible(false);

							}
						}
					} catch (Exception e) {
					}
				}
			}
			if (SqlCommandType.UPDATE == sqlCommandType) {
				LoginUserBasicDto sysUser = this.getLoginUser();
				Field[] fields = null;
				if (parameter instanceof ParamMap) {
					ParamMap<?> p = (ParamMap<?>) parameter;
					//update-begin-author:scott date:20190729 for:批量更新报错issues/IZA3Q--
					if (p.containsKey("et")) {
						parameter = p.get("et");
					} else {
						parameter = p.get("param1");
					}
					//update-end-author:scott date:20190729 for:批量更新报错issues/IZA3Q-

					//update-begin-author:scott date:20190729 for:更新指定字段时报错 issues/#516-
					if (parameter == null) {
						return invocation.proceed();
					}
					//update-end-author:scott date:20190729 for:更新指定字段时报错 issues/#516-

					fields = oConvertUtils.getAllFields(parameter);
				} else {
					fields = oConvertUtils.getAllFields(parameter);
				}

				for (Field field : fields) {
					log.debug("------field.name------" + field.getName());
					try {
						if ("lastupdateName".equals(field.getName())) {
							//获取登录用户信息
							if (sysUser != null) {
								// 登录账号
								field.setAccessible(true);
								field.set(parameter, sysUser.getName());
								field.setAccessible(false);
							}
						}
//						if ("lastupdateTime".equals(field.getName())) {
//							if(field.getAnnotation(SetLastupdateTime.class)==null) {
//								field.setAccessible(true);
//								field.set(parameter, null);
//								field.setAccessible(false);
//							}
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return invocation.proceed();
		}finally {
			long endTime = System.currentTimeMillis();
			long runTime= endTime - startTime;
			if(runTime>1000)
				log.info("超长sql执行:"+runTime);
			log.info("执行SQL "+sqlCommandType+":花费{}ms", (endTime - startTime));
		}


	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		// TODO Auto-generated method stub
	}

	private LoginUserBasicDto getLoginUser() {
		LoginUserBasicDto sysUser = null;
		try {
			sysUser = loginUtilsService.getLoginUserInfo();
		} catch (Exception e) {
			//e.printStackTrace();
			sysUser = null;
		}
		return sysUser;
	}

}
