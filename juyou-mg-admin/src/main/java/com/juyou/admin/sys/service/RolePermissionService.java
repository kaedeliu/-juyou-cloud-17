package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionDeleteDto;
import com.juyou.admin.sys.entity.RolePermission;

import java.util.List;

/**
 * 角色菜单服务接口
 * 
 * @author kaedeliu
 *
 */
public interface RolePermissionService {

	/**
	 * 添加角色菜单
	 * 
	 * @param userAddDto 角色菜单添加对象
	 */
	public void insert(RolePermissionAddDto userRoleAddDto);

	/**
	 * 批量删除角色菜单 菜单ID集合和角色ID集合同时存在时,按和角色ID集合删除角色菜单
	 * 
	 * @param userAddDto 删除角色菜单添加对象
	 */
	public void deletes(RolePermissionDeleteDto userRoleDelateDto);

	/**
	 * 根据角色ID查询菜单
	 * 
	 * @param roleId 角色ID
	 * @return 菜单ID集合
	 */
	List<String> queryByRoleId(String roleId);

	List<RolePermission> get(Wrapper<RolePermission> wrapper);
}
