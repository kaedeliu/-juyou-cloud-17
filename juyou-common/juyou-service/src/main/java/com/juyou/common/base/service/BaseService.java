package com.juyou.common.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.baomidou.mybatisplus.extension.service.IService;
import com.juyou.common.base.entity.BaseEntity;
import com.juyou.common.log.Operation;
import com.juyou.common.permissiondata.PermissionDataRule;
import com.juyou.common.task.CommonTask;


public interface BaseService extends IService<BaseEntity>{
	
	/**
	 * 获取所有数据权限
	 * @return
	 */
	Map<String,List<PermissionDataRule>> getAllPermissionData();

	/**
	 * 获取用户角色
	 * @param id
	 * @return
	 */
	Set<String> queryUserRole(String id);
	

	/**
	 * 查看角色是否有权限
	 * @param roleId
	 * @param permission
	 * @return
	 */
	boolean roleHasPermission(String roleId,String permission);



	Set<String> getRolePermissions(String roleCode);
	
	/**
	 * 读取用户数据权限
	 */
	Set<String> queryUserDataRule(String userCode);
	
	
	
//	/**
//	 * 获取登录用户的信息，主要用于处理数据权限
//	 * @param userId 用户编号
//	 * @return
//	 */
//	 LoginUserDto queryLoginUserInfo(String userId);
//
//	/**
//	 * 获取客户端登录用户的信息，主要用于处理数据权限
//	 * @param userId 用户编号
//	 * @return
//	 */
//	LoginUserDto queryCcLoginUserInfo(String userId);
//
	
	/**
	 * 保存操作日志
	 * @param operations
	 */
	void saveOperation(List<Operation> operations);
	
	/**
	 * 获取定时任务
	 * @param type
	 * @return
	 */
	List<CommonTask> selectCommonTask(String type, Integer status);
	


	/**
	 * 业务主键
	 * @param pkType
	 * @param increasingVal
	 */
	 void updatePkNum(String pkType,int increasingVal);
	 
	 /**
	  * 查询业务主键
	  * @param pkType
	  * @return
	  */
	 Integer selectPkByType(String pkType);

	/**
	 * 获取数据库服务器时间
	 * @return
	 */
	Date getDbTime();
	 
	 
}
