package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.role.RoleAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionAddDto;
import com.juyou.admin.sys.entity.Role;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.SelectDto;
import com.juyou.common.enums.StatusEnum;

import java.util.List;

/**
 * 角色服务接口
 * 
 * @author kaedeliu
 *
 */
public interface RoleService {

	/**
	 * 分页查询角色
	 * 
	 * @return 角色集合
	 */
	public IPage<Role> pageList(com.juyou.admin.sys.dto.role.RolePageDto rolePage) throws Exception;

	/**
	 * 查询角色详细
	 * 
	 * @param dto 角色ID
	 * @return 角色
	 */
	public com.juyou.admin.sys.dto.role.RoleDto findById(IdDto dto);

	/**
	 * 添加角色
	 * 
	 * @param roleAddDto 角色添加对象
	 */
	public void insert(RoleAddDto roleAddDto, StatusEnum statusEnum);

	/**
	 * 修改角色
	 * 
	 * @param roleEditDto 角色修改对象
	 */
	public void update(com.juyou.admin.sys.dto.role.RoleEditDto roleEditDto);

	/**
	 * 删除角色
	 * 
	 * @param dto 角色ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除角色
	 * 
	 * @param dtos 角色ID集合
	 */
	public void deletes(IdsDto dtos);

	/**
	 * 查询角色下拉列表框
	 * @return
	 */
	List<SelectDto> findRoleSelect() throws Exception;

	/**
	 * 查询角色拥有的权限
	 * @param idDto
	 * @return
	 */
	com.juyou.admin.sys.dto.role.RolePermissionDto findRolePermission(IdDto idDto) throws Exception;

	/**
	 * 更新角色授权
	 * @param rolePermissionAddDto
	 */
	void updateAuth( RolePermissionAddDto rolePermissionAddDto);
}
