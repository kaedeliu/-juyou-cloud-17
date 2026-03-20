package com.juyou.admin.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.role.RoleAddDto;
import com.juyou.admin.sys.dto.rolepermission.RolePermissionAddDto;
import com.juyou.admin.sys.entity.Role;
import com.juyou.admin.sys.service.RolePermissionService;
import com.juyou.admin.sys.service.RoleService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.dto.SelectDto;
import com.juyou.common.enums.StatusEnum;
import com.juyou.common.log.AutoLog;
import com.juyou.common.tenants.Tenants;
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
@Tag(name = "角色")
@RequestMapping("/sys/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	@Resource
	private RolePermissionService rolePermissionService;

	@Operation(summary = "角色-列表", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "角色-列表", db = false)
	@PostMapping("/pageList")
	@Tenants
	public Result<IPage<Role>> pageList(@RequestBody @Validated com.juyou.admin.sys.dto.role.RolePageDto rolePage) throws Exception {
		IPage<Role> pageList = this.roleService.pageList(rolePage);
		return new Result<>(pageList);
	}

	@Operation(summary = "角色-详情", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "角色-详情", db = false)
	@PostMapping("/findById")
	public Result<com.juyou.admin.sys.dto.role.RoleDto> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(roleService.findById(dto));
	}

	@Operation(summary = "角色-添加", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "角色-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody RoleAddDto roleAddDto) {
		this.roleService.insert(roleAddDto, StatusEnum.系统类型_默认);
		return Result.OK();
	}

	@Operation(summary = "角色-修改", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "角色-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody com.juyou.admin.sys.dto.role.RoleEditDto roleEditDto) {
		this.roleService.update(roleEditDto);
		return Result.OK();
	}

	@Operation(summary = "角色-删除", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "角色-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.roleService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "角色-批量删除", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "角色-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.roleService.deletes(dto);
		return Result.OK();
	}

	@Operation(summary = "角色-获取角色下拉列表数据", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "角色-获取角色下拉列表数据", db = false)
	@PostMapping("/findRoleSelect")
	@Tenants
	public Result<List<SelectDto>> findRoleSelect() throws Exception {
		return new Result<>(this.roleService.findRoleSelect());
	}

	@Operation(summary = "角色-获取角色拥有的权限", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "角色-获取角色拥有的权限", db = false)
	@PostMapping("/findRolePermission")
	public Result<com.juyou.admin.sys.dto.role.RolePermissionDto> findRolePermission(@Validated @RequestBody IdDto dto) throws Exception {
		com.juyou.admin.sys.dto.role.RolePermissionDto rolePermissionDto=this.roleService.findRolePermission(dto);
		return new Result<>(rolePermissionDto);
	}

	@Operation(summary = "角色-授权", tags = { "角色" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "角色-授权", db = false)
	@PostMapping("/updateAuth")
	public Result<Object> updateAuth(@Validated @RequestBody RolePermissionAddDto rolePermissionAddDto) {
		this.roleService.updateAuth(rolePermissionAddDto);
		return Result.OK();
	}

}
