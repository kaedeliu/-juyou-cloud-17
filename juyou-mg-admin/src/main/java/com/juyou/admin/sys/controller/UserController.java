package com.juyou.admin.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.user.UpdateMyPasswordDto;
import com.juyou.admin.sys.entity.User;
import com.juyou.admin.sys.service.UserService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.tenants.Tenants;
import com.juyou.common.utils.IPUtils;
import com.juyou.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "用户")
@RequestMapping("/sys/user")
public class UserController {

	@Resource
	private UserService userService;

	@Operation(summary = "用户-列表", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "用户-列表", db = false)
	@PostMapping("/pageList")
	@Tenants
	public Result<IPage<User>> pageList(@RequestBody @Validated com.juyou.admin.sys.dto.user.UserPageDto userPage) throws Exception {
		IPage<User> pageList = this.userService.pageList(userPage);
		return new Result<>(pageList);
	}

	@Operation(summary = "用户-详情", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "用户-详情", db = false)
	@PostMapping("/findById")
	public Result<com.juyou.admin.sys.dto.user.UserDto> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(userService.findById(dto));
	}

	@Operation(summary = "用户-添加", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "用户-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody com.juyou.admin.sys.dto.user.UserAddDto userAddDto) {
		this.userService.insert(userAddDto);
		return Result.OK();
	}

	@Operation(summary = "用户-修改", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "用户-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody com.juyou.admin.sys.dto.user.UserEditDto userEditDto) {
		this.userService.update(userEditDto);
		return Result.OK();
	}

	@Operation(summary = "用户-删除", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "用户-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.userService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "用户-批量删除", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "用户-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.userService.deletes(dto);
		return Result.OK();
	}

	@Operation(summary = "用户-修改密码", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "用户-修改密码", db = false)
	@PostMapping("/updatePassword")
	public Result<Object> updatePassword(@Validated @RequestBody com.juyou.admin.sys.dto.user.UpdatePasswordDto updatePasswordDto) {
		this.userService.updatePassword(updatePasswordDto);
		return Result.OK();
	}

	@Operation(summary = "用户-修改自己密码", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "用户-修改自己密码", db = false)
	@PostMapping("/updateMyPassword")
	public Result<Object> updateMyPassword(@Validated @RequestBody UpdateMyPasswordDto updateMyPasswordDto) {
		this.userService.updateMyPassword(updateMyPasswordDto);
		return Result.OK();
	}


	@Operation(summary = "用户-登录", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "用户-登录", db = false)
	@PostMapping("/login")
	public Result<com.juyou.admin.sys.dto.user.UserDto> login(@Validated @RequestBody com.juyou.admin.sys.dto.user.LoginDto loginDto, HttpServletRequest request) {
		// 设置IP
		loginDto.setIp(IPUtils.getIpAddr(request));
		com.juyou.admin.sys.dto.user.UserDto userDto = this.userService.login(loginDto);
		return Result.OK(userDto);
	}

	@Operation(summary = "用户-用户拥有权限", tags = { "用户" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "用户-用户拥有权限", db = false)
	@PostMapping("/findUserPermission")
	public Result<com.juyou.admin.sys.dto.user.UserPermissionAllDto> findUserPermission() throws Exception {
		return Result.OK(this.userService.findUserPermission());
	}
}
