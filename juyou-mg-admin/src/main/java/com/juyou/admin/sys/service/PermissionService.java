package com.juyou.admin.sys.service;

import java.util.List;

import com.juyou.admin.sys.dto.permission.PermissionAddDto;
import com.juyou.admin.sys.dto.permission.PermissionEditDto;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

/**
 * 菜单服务接口
 * 
 * @author kaedeliu
 *
 */
public interface PermissionService {

	/**
	 * 查询菜单
	 * 
	 * @return 菜单集合
	 */
	public List<Permission> list();
	
	/**
	 * 查询菜单详细
	 * 
	 * @param dto 菜单ID
	 * @return 菜单
	 */
	public Permission findById(IdDto dto);

	/**
	 * 添加菜单
	 * 
	 * @param permissionAddDto 菜单添加对象
	 */
	public void insert(PermissionAddDto permissionAddDto);

	/**
	 * 修改菜单
	 * 
	 * @param permissionEditDto 菜单修改对象
	 */
	public void update(PermissionEditDto permissionEditDto);

	/**
	 * 删除菜单
	 * 
	 * @param dto 菜单ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除菜单
	 * 
	 * @param dto 菜单ID集合
	 */
	public void deletes(IdsDto dtos);
}
