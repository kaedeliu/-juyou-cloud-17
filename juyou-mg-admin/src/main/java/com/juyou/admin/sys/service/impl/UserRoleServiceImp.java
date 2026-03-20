package com.juyou.admin.sys.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.juyou.admin.sys.dto.userrole.UserRoleAddDto;
import com.juyou.admin.sys.dto.userrole.UserRoleDeleteDto;
import com.juyou.admin.sys.entity.UserRole;
import com.juyou.admin.sys.mapper.UserRoleMapper;
import com.juyou.admin.sys.service.UserRoleService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.exception.BaseException;

@Service("userRoleService")
public class UserRoleServiceImp implements UserRoleService {

	@Resource
	private UserRoleMapper userRoleMapper;

	@Override
	@Transactional
	public void insert(UserRoleAddDto userRoleAddDto) {
		// 根据用户删除以前数据
		LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<UserRole>();
		lambdaQueryWrapper.eq(UserRole::getUserId, userRoleAddDto.getUserId());
		this.userRoleMapper.delete(lambdaQueryWrapper);

		// 添加新的
		List<String> roleIds = userRoleAddDto.getRoleIds();
		for (String roleId : roleIds) {
			UserRole userRole = new UserRole();
			userRole.setUserId(userRoleAddDto.getUserId());
			userRole.setRoleId(roleId);
			this.userRoleMapper.insert(userRole);
		}
	}

	@Override
	public void deletes(UserRoleDeleteDto userRoleDelateDto) {
		if ((null != userRoleDelateDto.getUserIds() && userRoleDelateDto.getUserIds().size() > 0) || (null != userRoleDelateDto.getRoleIds() && userRoleDelateDto.getRoleIds().size() > 0)) {
			LambdaQueryWrapper<UserRole> lambdaQueryWrapper = new LambdaQueryWrapper<UserRole>();
			if (null != userRoleDelateDto.getRoleIds() && userRoleDelateDto.getRoleIds().size() > 0) {
				lambdaQueryWrapper.clear();
				lambdaQueryWrapper.in(UserRole::getRoleId, userRoleDelateDto.getRoleIds());
			}
			if (null != userRoleDelateDto.getUserIds() && userRoleDelateDto.getUserIds().size() > 0) {
				lambdaQueryWrapper.clear();
				lambdaQueryWrapper.in(UserRole::getUserId, userRoleDelateDto.getUserIds());
			}
			this.userRoleMapper.delete(lambdaQueryWrapper);
		}
	}

	@Override
	public List<String> queryByUserId(String userId) {
		if (StringUtil.isBlank(userId)) {
			throw BaseException.defaultCode("用户ID不能为空");
		}
		return this.userRoleMapper.queryByUserId(userId);
	}
}
