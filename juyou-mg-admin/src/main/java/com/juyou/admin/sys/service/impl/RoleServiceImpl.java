package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.role.RoleAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionDeleteDto;
import com.juyou.admin.sys.dto.userrole.UserRoleDeleteDto;
import com.juyou.admin.sys.entity.Role;
import com.juyou.admin.sys.entity.RolePermission;
import com.juyou.admin.sys.mapper.RoleMapper;
import com.juyou.admin.sys.service.RolePermissionService;
import com.juyou.admin.sys.service.RoleService;
import com.juyou.admin.sys.service.UserRoleService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.dto.SelectDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.exception.BaseException;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.SelectUtils;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Resource
	private RoleMapper roleMapper;

	@Resource
	private RolePermissionService rolePermissionService;

	@Resource
	private UserRoleService userRoleService;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Resource
	private RedisUtil redisUtil;


	@Override
	public IPage<Role> pageList(com.juyou.admin.sys.dto.role.RolePageDto rolePage) throws Exception {
		Page<Role> page = new Page<>(rolePage.getPageNo(), rolePage.getPageSize());
		QueryWrapper<Role> queryWrapper=QueryGenerator.initQueryWrapper(rolePage,Role.class);

		if(loginUtilsService.getLoginUserInfo().getUserType()!=StatusEnum.USER_TYPE_系统超管.getCode()){
			queryWrapper.eq("sys_type",StatusEnum.系统类型_默认.getCode());
		}

		return this.roleMapper.selectPage(page, queryWrapper);
	}

	@Override
	public com.juyou.admin.sys.dto.role.RoleDto findById(IdDto dto) {
		Role role = this.roleMapper.selectById(dto.getId());
		com.juyou.admin.sys.dto.role.RoleDto roleDto = new com.juyou.admin.sys.dto.role.RoleDto();
		BeanUtils.copyProperties(role,roleDto);
		return roleDto.setPermissionIds(this.rolePermissionService.queryByRoleId(dto.getId()));
	}

	@Override
	public void insert(RoleAddDto roleAddDto, StatusEnum statusEnum) {
		Role role = new Role();
		if(CommonConstant.SYS_ADMIN.equals( roleAddDto.getCode()) || CommonConstant.TENANTS_ADMIN.equals(roleAddDto.getCode())){
			throw BaseException.defaultCode("编码[" + roleAddDto.getCode() + "]重复");
		}
		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>();
		queryWrapper.eq(Role::getCode, roleAddDto.getCode()).eq(Role::getTenantsId,loginUserDto.getTenantsId()); // 获取机构所属编码
		Long count = this.roleMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + roleAddDto.getCode() + "]重复");
		}
		queryWrapper.clear();
		queryWrapper.eq(Role::getName, roleAddDto.getName()).eq(Role::getTenantsId, loginUserDto.getTenantsId());
		count = this.roleMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + roleAddDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(roleAddDto, role);
		role.setSysType(statusEnum.getCode());

		if (null == role.getStatus()) {
			role.setStatus(Status.启用.getId()); // 默认禁用
		}
		this.roleMapper.insert(role);
	}

	@Override
	public void update(com.juyou.admin.sys.dto.role.RoleEditDto roleEditDto) {
		Role role = new Role();
		Role old=getOldAndVfEdit(roleEditDto.getRoleId());

		if(CommonConstant.SYS_ADMIN.equals( old.getCode()) || CommonConstant.TENANTS_ADMIN.equals(old.getCode())){
			throw BaseException.defaultCode("系统角色不能编辑");
		}

		if(CommonConstant.SYS_ADMIN.equals( roleEditDto.getCode()) || CommonConstant.TENANTS_ADMIN.equals(roleEditDto.getCode())){
			throw BaseException.defaultCode("编码[" + roleEditDto.getCode() + "]重复");
		}

		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		// 验证编码名称否重复
		LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<Role>();
		queryWrapper.eq(Role::getCode, roleEditDto.getCode()).eq(Role::getTenantsId, loginUserDto.getTenantsId()).ne(Role::getRoleId, roleEditDto.getRoleId());
		Long count = this.roleMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + roleEditDto.getCode() + "]重复");
		}
		queryWrapper.clear();
		queryWrapper.eq(Role::getName, roleEditDto.getName()).ne(Role::getRoleId, roleEditDto.getRoleId()).eq(Role::getTenantsId, loginUserDto.getTenantsId());
		count = this.roleMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("名称[" + roleEditDto.getName() + "]重复");
		}
		BeanUtils.copyProperties(roleEditDto, role);


		String oldCode = old.getCode();

		this.roleMapper.updateById(role);
		if(!oldCode.equals(role.getCode()) || !roleEditDto.getStatus().equals(old.getStatus()))  //角色编码、状态有改变则删除缓存
			deleteRedisCache(role.getCode(),role.getTenantsId());
	}

	@Override
	@Transactional
	public void delete(IdDto dto) {
		deleteById(dto.getId());
	}

	private void deleteById(String roleId){
		Role old=getOldAndVfEdit(roleId);
		String oldCode = old.getCode();
		// 删除角色菜单
		RolePermissionDeleteDto rolePermissionDeleteDto = new RolePermissionDeleteDto();
		rolePermissionDeleteDto.setRoleIds(Collections.singletonList(roleId));
		this.rolePermissionService.deletes(rolePermissionDeleteDto);

		// 删除用户角色
		UserRoleDeleteDto userRoleDeleteDto = new UserRoleDeleteDto();
		userRoleDeleteDto.setRoleIds(Collections.singletonList(roleId));
		this.userRoleService.deletes(userRoleDeleteDto);

		this.roleMapper.deleteById(roleId);

		deleteRedisCache(oldCode,old.getTenantsId());
	}

	@Override
	@Transactional
	public void deletes(IdsDto dtos) {
		for (String roleId:dtos.getIds()  ) {
			deleteById(roleId);
		}
//		// 删除角色菜单
//		RolePermissionDeleteDto rolePermissionDeleteDto = new RolePermissionDeleteDto();
//		rolePermissionDeleteDto.setRoleIds(Arrays.asList(dtos.getIds()));
//		this.rolePermissionService.deletes(rolePermissionDeleteDto);
//
//		// 删除用户角色
//		UserRoleDeleteDto userRoleDeleteDto = new UserRoleDeleteDto();
//		userRoleDeleteDto.setRoleIds(Arrays.asList(dtos.getIds()));
//		this.userRoleService.deletes(userRoleDeleteDto);
//
//		this.roleMapper.deleteBatchIds(Arrays.asList(dtos.getIds()));
	}

	@Override
	public List<SelectDto> findRoleSelect() throws Exception {
		QueryWrapper<Role> queryWrapper= QueryGenerator.initQueryWrapper(new Role(),Role.class);
		queryWrapper.eq("status", StatusEnum.是.getCode())
				.select("role_id","name","code");
		List<Role> roles=this.roleMapper.selectList(queryWrapper);
		return SelectUtils.fromatSelect(roles,"name","roleId", Role.class,"code");
	}

	@Override
	public com.juyou.admin.sys.dto.role.RolePermissionDto findRolePermission(IdDto idDto) throws Exception {
		QueryWrapper<RolePermission> queryWrapper=QueryGenerator.initQueryWrapper(new RolePermission(),RolePermission.class);
		queryWrapper.eq("role_id",idDto.getId())
				.select("permission_id");
		List<RolePermission> rolePermissions=this.rolePermissionService.get(queryWrapper);
		com.juyou.admin.sys.dto.role.RolePermissionDto rolePermissionDto=new com.juyou.admin.sys.dto.role.RolePermissionDto();
		if(rolePermissions!=null && !rolePermissions.isEmpty()) {
			rolePermissionDto.setPermissionIds(rolePermissions.stream().map(RolePermission::getPermissionId).distinct().collect(Collectors.toList()));
		}
		return rolePermissionDto;
	}

	private Role getOldAndVfEdit(String roleId){
		Role old=this.roleMapper.selectById(roleId);
		if(old==null) throw new BaseException("角色信息异常或者已经删除");
		if(old.getSysType() != StatusEnum.系统类型_默认.getCode()){
			throw new BaseException("系统预留角色，不能编辑或删除");
		}
		return  old;
	}

	@Override
	public void updateAuth(RolePermissionAddDto rolePermissionAddDto) {
		Role role=this.roleMapper.selectById(rolePermissionAddDto.getRoleId());
		if(role==null) throw BaseException.defaultCode("角色信息异常或者已经删除");
		rolePermissionService.insert(rolePermissionAddDto);
		deleteRedisCache(role.getCode(),role.getTenantsId());
	}

	private void deleteRedisCache(String roleCode,String tenantsId){
		String reidsKey= CommonConstant.SYS_ROLE_AUTH+CommonConstant.REDIS_BUFF+tenantsId+CommonConstant.REDIS_BUFF+roleCode;
		redisUtil.del(reidsKey);
	}
}
