package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juyou.admin.sys.constant.PermissionType;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.permission.PermissionAddDto;
import com.juyou.admin.sys.dto.permission.PermissionEditDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionDeleteDto;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.admin.sys.mapper.PermissionMapper;
import com.juyou.admin.sys.service.PermissionService;
import com.juyou.admin.sys.service.RolePermissionService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.service.LoginUtilsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

	@Resource
	private PermissionMapper permissionMapper;

	@Resource
	private RolePermissionService rolePermissionService;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Override
	public List<Permission> list() {
		LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<Permission>();

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();

		if(loginUserDto.getUserType()!=StatusEnum.USER_TYPE_系统超管.getCode() ) //不是超级管理管
			queryWrapper.eq(Permission::getSysType,StatusEnum.系统类型_默认.getCode());

		List<Permission> menus = this.permissionMapper.selectList(queryWrapper);
		menus = packagTree(menus, StringUtil.TREE_ROOT_ID);
		return menus;
	}

	@Override
	public Permission findById(IdDto dto) {
		return this.permissionMapper.selectById(dto.getId());
	}

	@Override
	public void insert(PermissionAddDto permissionAddDto) {
		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();

		Permission permission = new Permission();
		BeanUtils.copyProperties(permissionAddDto, permission);
		if (null == permission.getStatus()) {
			permission.setStatus(Status.禁用.getId()); // 默认禁用
		}
		if (null == permission.getType()) {
			permission.setType(PermissionType.一级菜单.getId()); // 默认一级菜单
		}
		if (PermissionType.一级菜单.getId().equals(permission.getType())) {
			permission.setParentId(StringUtil.TREE_ROOT_ID);
		} else {
			if (StringUtil.isBlank(permission.getParentId())) {
				throw BaseException.defaultCode("父ID不能为空");
			} else {
				Permission parent = this.permissionMapper.selectById(permission.getParentId());
				if (null == parent) {
					throw BaseException.defaultCode("父级菜单不存在");
				}
			}
		}
		if (PermissionType.按钮权限.getId().equals(permission.getType()) && StringUtil.isBlank(permission.getAuthUrl())) {
			throw BaseException.defaultCode("权限标识不能为空");
		}
		if (null == permission.getSequence()) {
			// 自动获取下一个排序号
			LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<Permission>();
			queryWrapper.eq(Permission::getParentId, permission.getParentId());
			queryWrapper.orderByDesc(Permission::getSequence);
			queryWrapper.last(StringUtil.LIMIT_1);
			Permission mnu = this.permissionMapper.selectOne(queryWrapper);
			if (null == mnu) {
				permission.setSequence(1);
			} else {
				permission.setSequence(null == mnu.getSequence() ? 1 : mnu.getSequence() + 1);
			}
		}
		this.permissionMapper.insert(permission);
	}

	@Override
	public void update(PermissionEditDto permissionEditDto) {
		Permission permission = new Permission();
		BeanUtils.copyProperties(permissionEditDto, permission);
		this.permissionMapper.updateById(permission);
	}

	@Override
	@Transactional
	public void delete(IdDto dto) {
		// 判断是否存在子集
		LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<Permission>();
		queryWrapper.eq(Permission::getParentId, dto.getId());
		Long count = this.permissionMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("存在子菜单不能删除");
		}
		// 删除角色菜单
		RolePermissionDeleteDto rolePermissionDeleteDto = new RolePermissionDeleteDto();
		rolePermissionDeleteDto.setPermissionIds(Arrays.asList(dto.getId()));
		this.rolePermissionService.deletes(rolePermissionDeleteDto);
		this.permissionMapper.deleteById(dto.getId());
	}

	@Override
	@Transactional
	public void deletes(IdsDto dtos) {
		// 判断是否存在子集
		String[] ids = dtos.getIds();
		LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<Permission>();
		for (String id : ids) {
			queryWrapper.clear();
			queryWrapper.eq(Permission::getParentId, id);
			Long count = this.permissionMapper.selectCount(queryWrapper);
			if (count > 0) {
				Permission permission = this.permissionMapper.selectById(id);
				throw BaseException.defaultCode("[" + permission.getName() + "]存在子菜单不能删除");
			}
		}
		// 删除角色菜单
		RolePermissionDeleteDto rolePermissionDeleteDto = new RolePermissionDeleteDto();
		rolePermissionDeleteDto.setPermissionIds(Arrays.asList(dtos.getIds()));
		this.rolePermissionService.deletes(rolePermissionDeleteDto);
		this.permissionMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));
	}

	/**
	 * 树形结构封装
	 *
	 * @param data         查询结果集
	 * @param rootParentId 根节点父ID
	 * @return 树形结构结果集
	 */
	private List<Permission> packagTree(List<Permission> data, String rootParentId) {
		List<Permission> treeDtos = new ArrayList<Permission>();
		treeDtos = analyzeTree(data, rootParentId);
		return treeDtos;
	}

	/**
	 * 组装树
	 *
	 * @param data     查询结果集
	 * @param parentId 根节点父ID
	 * @return 树形结构结果集
	 */
	private List<Permission> analyzeTree(List<Permission> data, String parentId) {
		List<Permission> treeDtos = new ArrayList<Permission>();
		List<Permission> temp = new ArrayList<Permission>(data);
		if (data != null && data.size() > 0) {
			for (Permission permission : data) {
				if (parentId.equals(permission.getParentId())) {
					Permission mnu = new Permission();
					BeanUtils.copyProperties(permission, mnu);
					temp.remove(permission);
					mnu.setChildren(analyzeTree(temp, permission.getPermissionId()));
					treeDtos.add(mnu);
				}
			}
		}
		return treeDtos;
	}
}
