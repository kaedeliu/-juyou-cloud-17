package com.juyou.admin.sys.controller;

import com.juyou.admin.sys.dto.permission.PermissionAddDto;
import com.juyou.admin.sys.dto.permission.PermissionEditDto;
import com.juyou.admin.sys.entity.Permission;
import com.juyou.admin.sys.service.PermissionService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Tag(name = "菜单")
@RequestMapping("/sys/permission")
public class PermissionController {

	@Resource
	PermissionService permissionService;

	@Operation(summary = "-列表", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "菜单-列表", db = false)
	@PostMapping("/list")
	public Result<List<Permission>> list() {
		List<Permission> list = this.permissionService.list();
		return new Result<>(list);
	}

	@Operation(summary = "菜单-详情", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "菜单-详情", db = false)
	@PostMapping("/findById")
	public Result<Permission> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(permissionService.findById(dto));
	}

	@Operation(summary = "菜单-添加", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "菜单-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody PermissionAddDto permissionAddDto) {
		this.permissionService.insert(permissionAddDto);
		return Result.OK();
	}

	@Operation(summary = "菜单-修改", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "菜单-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody PermissionEditDto permissionEditDto) {
		this.permissionService.update(permissionEditDto);
		return Result.OK();
	}

	@Operation(summary = "菜单-删除", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "菜单-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.permissionService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "菜单-批量删除", tags = { "菜单" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "菜单-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.permissionService.deletes(dto);
		return Result.OK();
	}
}
