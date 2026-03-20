package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionDeleteDto;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.admin.sys.entity.RolePermission;
import com.juyou.admin.sys.mapper.PermissionMapper;
import com.juyou.admin.sys.mapper.RolePermissionMapper;
import com.juyou.admin.sys.service.RolePermissionService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.service.LoginUtilsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("rolePermissionService")
public class RolePermissionServiceImpl implements RolePermissionService {

	@Resource
	private RolePermissionMapper rolePermissionMapper;

	@Resource
	private PermissionMapper permissionMapper;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Override
	@Transactional
	public void insert(RolePermissionAddDto rolePermissionAddDto) {
		// 根据角色删除以前数据
		LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = new LambdaQueryWrapper<RolePermission>();
		lambdaQueryWrapper.eq(RolePermission::getRoleId, rolePermissionAddDto.getRoleId());
		this.rolePermissionMapper.delete(lambdaQueryWrapper);

		// 添加新的
		List<String> permissionIds = rolePermissionAddDto.getPermissionIds();
		if(permissionIds==null || permissionIds.isEmpty()) return;
		if(loginUtilsService.getLoginUserInfo().getUserType()!=StatusEnum.USER_TYPE_系统超管.getCode()) {
			LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper<>();
			queryWrapper.ne(Permission::getSysType, StatusEnum.USER_TYPE_系统超管.getCode())
					.in(Permission::getPermissionId,permissionIds)
					.select(Permission::getPermissionId);
			List<Permission> permissions = permissionMapper.selectList(queryWrapper);
			permissionIds = permissions.stream().map(Permission::getPermissionId).collect(Collectors.toList());
		}

		for (String permissionId : permissionIds) {
			RolePermission rolePermission = new RolePermission();
			rolePermission.setPermissionId(permissionId);
			rolePermission.setRoleId(rolePermissionAddDto.getRoleId());
			this.rolePermissionMapper.insert(rolePermission);
		}
	}

	@Override
	public void deletes(RolePermissionDeleteDto rolePermissionDelateDto) {
		if ((null != rolePermissionDelateDto.getPermissionIds() && rolePermissionDelateDto.getPermissionIds().size() > 0)
				|| (null != rolePermissionDelateDto.getRoleIds() && rolePermissionDelateDto.getRoleIds().size() > 0)) {
			LambdaQueryWrapper<RolePermission> lambdaQueryWrapper = new LambdaQueryWrapper<RolePermission>();
			if (null != rolePermissionDelateDto.getRoleIds() && rolePermissionDelateDto.getRoleIds().size() > 0) {
				lambdaQueryWrapper.clear();
				lambdaQueryWrapper.in(RolePermission::getRoleId, rolePermissionDelateDto.getRoleIds());
			}
			if (null != rolePermissionDelateDto.getPermissionIds() && rolePermissionDelateDto.getPermissionIds().size() > 0) {
				lambdaQueryWrapper.clear();
				lambdaQueryWrapper.in(RolePermission::getPermissionId, rolePermissionDelateDto.getPermissionIds());
			}
			this.rolePermissionMapper.delete(lambdaQueryWrapper);
		}
	}

	@Override
	public List<String> queryByRoleId(String roleId) {
		if (StringUtil.isBlank(roleId)) {
			throw BaseException.defaultCode("角色ID不能为空");
		}
		return this.rolePermissionMapper.queryByRoleId(roleId);
	}

	@Override
	public List<RolePermission> get(Wrapper<RolePermission> wrapper) {
		return this.rolePermissionMapper.selectList(wrapper);
	}
}
