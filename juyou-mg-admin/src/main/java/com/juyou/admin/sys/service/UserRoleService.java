package com.juyou.admin.sys.service;

import java.util.List;

import com.juyou.admin.sys.dto.userrole.UserRoleAddDto;
import com.juyou.admin.sys.dto.userrole.UserRoleDeleteDto;

/**
 * 用户角色服务接口
 * 
 * @author kaedeliu
 *
 */
public interface UserRoleService {

	/**
	 * 添加用户角色
	 * 
	 * @param userAddDto 用户角色添加对象
	 */
	public void insert(UserRoleAddDto userRoleAddDto);

	/**
	 * 批量删除用户角色 用户ID集合和角色ID集合同时存在时,按用户ID集合删除用户角色
	 * 
	 * @param userAddDto 删除角色添加对象
	 */
	public void deletes(UserRoleDeleteDto userRoleDelateDto);

	/**
	 * 根据用户ID查询角色
	 * 
	 * @param userId 用户ID
	 * @return 角色集合
	 */
	List<String> queryByUserId(String userId);
}
