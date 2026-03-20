package com.juyou.admin.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.user.UpdateMyPasswordDto;
import com.juyou.admin.sys.entity.User;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;

/**
 * 用户服务接口
 * 
 * @author kaedeliu
 *
 */
public interface UserService {

	/**
	 * 分页查询用户
	 * 
	 * @return 用户集合
	 */
	public IPage<User> pageList(com.juyou.admin.sys.dto.user.UserPageDto userPageDto) throws Exception;

	/**
	 * 查询用户详细
	 * 
	 * @param dto 用户ID
	 * @return 用户
	 */
	public com.juyou.admin.sys.dto.user.UserDto findById(IdDto dto);

	/**
	 * 添加用户
	 * 
	 * @param userAddDto 用户添加对象
	 */
	public void insert(com.juyou.admin.sys.dto.user.UserAddDto userAddDto);

	/**
	 * 修改用户
	 * 
	 * @param UserEditDto 用户修改对象
	 */
	public void update(com.juyou.admin.sys.dto.user.UserEditDto userEditDto);

	/**
	 * 删除用户
	 * 
	 * @param dto 用户ID
	 */
	public void delete(IdDto dto);

	/**
	 * 批量删除用户
	 * 
	 * @param dto 用户ID集合
	 */
	public void deletes(IdsDto dtos);

	/**
	 * 修改密码
	 * 
	 * @param updatePasswordDto 修改密码参数对象
	 */
	public void updatePassword(com.juyou.admin.sys.dto.user.UpdatePasswordDto updatePasswordDto);

	/**
	 * 修改密码
	 *
	 * @param updateMyPasswordDto 修改密码参数对象
	 */
	public void updateMyPassword(UpdateMyPasswordDto updateMyPasswordDto);

	/**
	 * 登录
	 * 
	 * @param loginDto 用户名、密码
	 * @return 用户详细
	 */
	public com.juyou.admin.sys.dto.user.UserDto login(com.juyou.admin.sys.dto.user.LoginDto loginDto);


	/**
	 * 用户拥有的权限
	 * @return
	 */
	com.juyou.admin.sys.dto.user.UserPermissionAllDto findUserPermission() throws Exception;
}
