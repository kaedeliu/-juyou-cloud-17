package com.juyou.admin.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.admin.sys.constant.PermissionType;
import com.juyou.admin.sys.constant.Status;
import com.juyou.admin.sys.dto.user.UpdateMyPasswordDto;
import com.juyou.admin.sys.dto.userrole.UserRoleAddDto;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.admin.sys.entity.Tenants;
import com.juyou.admin.sys.entity.User;
import com.juyou.admin.sys.mapper.PermissionMapper;
import com.juyou.admin.sys.mapper.UserMapper;
import com.juyou.admin.sys.service.TenantsService;
import com.juyou.admin.sys.service.UserRoleService;
import com.juyou.admin.sys.service.UserService;
import com.juyou.admin.util.StringUtil;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.LoginUserBasicDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.exception.BaseException;
import com.juyou.common.jwt.JwtUtil;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.service.LoginUtilsService;
import com.juyou.common.utils.PasswordUtil;
import com.juyou.common.utils.TreeUtils;
import com.juyou.redis.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Resource
	private UserMapper userMapper;

	@Resource
	private EnvUtils envUtils;

	@Resource
	private RedisUtil redisUtil;

	@Resource
	private UserRoleService userRoleService;

	@Resource
	private LoginUtilsService loginUtilsService;

	@Resource
	private PermissionMapper permissionMapper;

	@Resource
	TenantsService tenantsService;

	@Override
	public IPage<User> pageList(com.juyou.admin.sys.dto.user.UserPageDto userPage) throws Exception {
		Page<User> page = new Page<>(userPage.getPageNo(), userPage.getPageSize());
		QueryWrapper<User> queryWrapper = QueryGenerator.initQueryWrapper(userPage, User.class);
		if (loginUtilsService.getLoginUserInfo().getUserType() != StatusEnum.USER_TYPE_系统超管.getCode()) {
			queryWrapper.eq("sys_type", StatusEnum.系统类型_默认.getCode());
		}
		return this.userMapper.selectPage(page, queryWrapper);
	}

	@Override
	public com.juyou.admin.sys.dto.user.UserDto findById(IdDto dto) {
		User user = this.userMapper.selectById(dto.getId());
		com.juyou.admin.sys.dto.user.UserDto userDto = new com.juyou.admin.sys.dto.user.UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setRoleIds(this.userRoleService.queryByUserId(dto.getId()));
		return userDto;
	}

	@Override
	@Transactional
	public void insert(com.juyou.admin.sys.dto.user.UserAddDto userAddDto) {
		User user = new User();
		if (userAddDto.getUserType() != StatusEnum.USER_TYPE_默认.getCode() && loginUtilsService.getLoginUserInfo().getUserType() != StatusEnum.USER_TYPE_系统超管.getCode()) {
			throw BaseException.defaultCode("账户信息异常，请联系管理员");
		}
		// 验证编码名称否重复
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
		queryWrapper.eq(User::getCode, userAddDto.getCode()); // 获取机构所属编码
		Long count = this.userMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + userAddDto.getCode() + "]重复");
		}
		BeanUtils.copyProperties(userAddDto, user);
		if (null == user.getStatus()) {
			user.setStatus(Status.启用.getId()); // 默认启用
		}
		if (null == user.getLockStatus()) {
			user.setLockStatus(Status.禁用.getId()); // 默认启用
		}
		user.setDelFlag(Status.禁用.getId());
		// 初始化密码
		String secret = StringUtil.randomGen(8);
		String encrypt = PasswordUtil.encrypt(user.getPassword(), user.getPassword(), secret);// 加密后的密码
		user.setSecret(secret);
		user.setPassword(encrypt);
		this.userMapper.insert(user);

		// 添加用户角色
		UserRoleAddDto userRoleAddDto = new UserRoleAddDto();
		userRoleAddDto.setUserId(user.getUserId());
		userRoleAddDto.setRoleIds(userAddDto.getRoleIds());
		this.userRoleService.insert(userRoleAddDto);
	}

	@Override
	public void update(com.juyou.admin.sys.dto.user.UserEditDto userEditDto) {
		User user = new User();
		// 验证编码名称否重复
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
		queryWrapper.eq(User::getCode, userEditDto.getCode()).ne(User::getUserId, userEditDto.getUserId());
		Long count = this.userMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw BaseException.defaultCode("编码[" + userEditDto.getCode() + "]重复");
		}
		BeanUtils.copyProperties(userEditDto, user);
		this.userMapper.updateById(user);

		deleteRedisCache(user.getUserId());
	}

	@Override
	public void delete(IdDto dto) {
		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.eq(User::getUserId, dto.getId());
		lambdaUpdateWrapper.set(User::getDelFlag, Status.启用.getId());
		lambdaUpdateWrapper.setSql("code=CONCAT(code,'_del')");
		this.userMapper.update(null, lambdaUpdateWrapper);
	}

	@Override
	public void deletes(IdsDto dtos) {
		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.in(User::getUserId, Arrays.asList(dtos.getIds()));
		lambdaUpdateWrapper.set(User::getDelFlag, Status.启用.getId());
		this.userMapper.update(null, lambdaUpdateWrapper);
	}

	@Override
	public void updatePassword(com.juyou.admin.sys.dto.user.UpdatePasswordDto updatePasswordDto) {
		User user = this.userMapper.selectById(updatePasswordDto.getUserId());
		if (null == user) {
			throw BaseException.defaultCode("用户不存在");
		}

		String secret = StringUtil.randomGen(8);
		String password = PasswordUtil.encrypt(updatePasswordDto.getNewPassword(), updatePasswordDto.getNewPassword(), secret);
		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.eq(User::getUserId, updatePasswordDto.getUserId());
		lambdaUpdateWrapper.set(User::getPassword, password);
		lambdaUpdateWrapper.set(User::getSecret, secret);
		this.userMapper.update(null, lambdaUpdateWrapper);
	}

	@Override
	public void updateMyPassword(UpdateMyPasswordDto updateMyPasswordDto) {
		User user = this.userMapper.selectById(loginUtilsService.getLoginUserInfo().getUserId());
		if (null == user) {
			throw BaseException.defaultCode("用户不存在");
		}
		String password = PasswordUtil.encrypt(updateMyPasswordDto.getOldPassword(), updateMyPasswordDto.getOldPassword(), user.getSecret());
		if (!password.equals(user.getPassword())) {
			throw BaseException.defaultCode("原密码错误");
		}
		String secret = StringUtil.randomGen(8);
		password = PasswordUtil.encrypt(updateMyPasswordDto.getNewPassword(), updateMyPasswordDto.getNewPassword(), secret);
		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
		lambdaUpdateWrapper.eq(User::getUserId, user.getUserId());
		lambdaUpdateWrapper.set(User::getPassword, password);
		lambdaUpdateWrapper.set(User::getSecret, secret);
		this.userMapper.update(null, lambdaUpdateWrapper);
	}

	@Override
	public com.juyou.admin.sys.dto.user.UserDto login(com.juyou.admin.sys.dto.user.LoginDto loginDto) {
		LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
		queryWrapper.eq(User::getCode, loginDto.getLoginName()).eq(User::getDelFlag, Status.禁用.getId());
		User user = this.userMapper.selectOne(queryWrapper); // TODO 需要单独处理缓存用户信息
		if (null == user) {
			throw BaseException.defaultCode("用户不存在");
		}
		if (Status.禁用.getId().equals(user.getStatus())) {
			throw BaseException.defaultCode("用户被禁用");
		}
		if (Status.启用.getId().equals(user.getLockStatus())) {
			throw BaseException.defaultCode("用户被锁定");
		}
		String password = PasswordUtil.encrypt(loginDto.getPassword(), loginDto.getPassword(), user.getSecret());// 将输入密码加密
		if (!password.equals(user.getPassword())) {
			// 锁定用户
			throw BaseException.defaultCode("密码错误");
		}
		Tenants tenants = tenantsService.getById(user.getTenantsId());
		if (tenants == null)
			throw BaseException.defaultCode("商户信息异常");
		if (tenants.getStatus() == StatusEnum.否.getCode())
			throw BaseException.defaultCode("商户过期或者异常");

		LambdaUpdateWrapper<User> lambdaUpdateWrapper = new LambdaUpdateWrapper<User>();
		lambdaUpdateWrapper.eq(User::getUserId, user.getUserId());
		lambdaUpdateWrapper.set(User::getLoginTime, new Date());
		lambdaUpdateWrapper.set(User::getLoginIp, loginDto.getIp());

		this.userMapper.update(null, lambdaUpdateWrapper);

		long expireTime = envUtils.value(EnvKey.系统登录超时时间, Long.class);
		String token = JwtUtil.sign(user.getUserId(), user.getUserType(), expireTime, envUtils.value(EnvKey.客户端登录Secrets));

		// 删除缓存信息
		deleteRedisCache(user.getUserId());

		com.juyou.admin.sys.dto.user.UserDto userDto = new com.juyou.admin.sys.dto.user.UserDto();
		BeanUtils.copyProperties(user, userDto);
		userDto.setToken(token);
		userDto.setTenantsName(tenants.getName());
		return userDto;
	}

	@Override
	public com.juyou.admin.sys.dto.user.UserPermissionAllDto findUserPermission() throws Exception {
		LoginUserBasicDto loginUserDto = loginUtilsService.getLoginUserInfo();
		List<Permission> permissions = null;
		if (loginUserDto.getRoleCodes().contains(CommonConstant.SYS_ADMIN) || loginUserDto.getRoleCodes().contains(CommonConstant.TENANTS_ADMIN)) {
			LambdaQueryWrapper<Permission> queryWrapper = new LambdaQueryWrapper();
			queryWrapper.eq(Permission::getStatus, StatusEnum.是.getCode());
			queryWrapper.orderByAsc(Permission::getSequence).groupBy(Permission::getPermissionId);
			if (!loginUserDto.getRoleCodes().contains(CommonConstant.SYS_ADMIN)) {
				queryWrapper.eq(Permission::getSysType, StatusEnum.系统类型_默认);
			}
			permissions = permissionMapper.selectList(queryWrapper);
		} else {
			QueryWrapper<User> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("p.status", StatusEnum.是.getCode());
			queryWrapper.orderByAsc("p.sequence");
			queryWrapper.groupBy("p.permission_id");
			queryWrapper.select("p.*");
			queryWrapper.eq("p.sys_type", StatusEnum.系统类型_默认.getCode());
			queryWrapper.eq("ur.user_id", loginUserDto.getUserId());
			permissions = userMapper.findUserPermission(queryWrapper);
		}

		// 超管不加条件
		com.juyou.admin.sys.dto.user.UserPermissionAllDto userPermissionAllDto = new com.juyou.admin.sys.dto.user.UserPermissionAllDto();

		List<com.juyou.admin.sys.dto.user.UserBtnPermissionDto> userBtnPermissionDtos = new ArrayList<>();
		if (!permissions.isEmpty()) {
			List<Permission> btns = permissions.stream().filter(item -> item.getType().equals(PermissionType.按钮权限.getId())).collect(Collectors.toList());
			if (!btns.isEmpty()) {
				permissions.removeAll(btns);
				btns.forEach(item -> {
					com.juyou.admin.sys.dto.user.UserBtnPermissionDto userBtnPermissionDto = new com.juyou.admin.sys.dto.user.UserBtnPermissionDto();
					BeanUtils.copyProperties(item, userBtnPermissionDto);
					userBtnPermissionDtos.add(userBtnPermissionDto);
				});
			}
		}
		List<com.juyou.admin.sys.dto.user.UserPermissionDto> userPermissionDtos = TreeUtils.formatTree(permissions, "0", "permissionId", com.juyou.admin.sys.dto.user.UserPermissionDto.class, Permission.class);
		return userPermissionAllDto.setUserPermissionDtos(userPermissionDtos).setUserBtnPermissionDtos(userBtnPermissionDtos);
	}

	/**
	 * 清除用户缓存
	 * 
	 * @param userId
	 */
	private void deleteRedisCache(String userId) {
		String redisKey = CommonConstant.SYS_LOGIN_USER + CommonConstant.REDIS_BUFF + userId;
		redisUtil.del(redisKey);
	}
}
