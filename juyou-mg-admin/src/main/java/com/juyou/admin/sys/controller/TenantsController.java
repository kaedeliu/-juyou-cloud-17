package com.juyou.admin.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.admin.sys.dto.tenants.TenantsAddDto;
import com.juyou.admin.sys.dto.tenants.TenantsEditDto;
import com.juyou.admin.sys.dto.tenants.TenantsPageDto;
import com.juyou.admin.sys.entity.Tenants;
import com.juyou.admin.sys.service.TenantsService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yx
 * @since 2023-04-21 12:10:51
 */
@Controller
@Tag(name = "租户管理")
@RequestMapping("/sys/tenants")
public class TenantsController {
	@Autowired
	TenantsService tenantsService;
	
	@Operation(summary = "租户管理-列表", tags = {"租户管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "租户管理--列表", db = false)
	@PostMapping("/pageList")
	//@Tenants //多租户
	public Result<IPage<Tenants>> pageList(@Validated @RequestBody TenantsPageDto tenantsPageDto) throws  Exception{
		QueryWrapper<Tenants> queryWrapper = QueryGenerator.initQueryWrapper(tenantsPageDto,Tenants.class);
        Page<Tenants> page = new Page<Tenants>(tenantsPageDto.getPageNo(), tenantsPageDto.getPageSize());
        IPage<Tenants> pageList = tenantsService.page(page, queryWrapper);
		return new Result<>(pageList);
	}
	
	
	@Operation(summary = "租户管理-详情", tags = {"租户管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "租户管理-详情", db = false)
	@PostMapping("/findById")
	public Result<Tenants> findById(@Validated @RequestBody IdDto dto){
		return Result.OK(tenantsService.getById(dto.getId()));
	}


	@Operation(summary = "租户管理-添加", tags = {"租户管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "租户管理--添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody TenantsAddDto tenantsAddDto){
		Tenants tenants = new Tenants();
		BeanUtils.copyProperties(tenantsAddDto,tenants);
		tenantsService.save(tenants);
		return Result.OK();
	}
	
	@Operation(summary = "租户管理-修改", tags = {"租户管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "租户管理-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody TenantsEditDto tenantsEditDto){
		Tenants tenants = new Tenants();
		BeanUtils.copyProperties(tenantsEditDto,tenants);
		tenantsService.updateById(tenants);
		return Result.OK();
	}

	@Operation(summary = "租户管理-初始化数据", tags = {"租户管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "租户管理-初始化数据", db = false)
	@PostMapping("/updateInitData")
	public Result<Object> updateInitData(@Validated @RequestBody IdDto idDto){
		tenantsService.updateInitData(idDto);
		return Result.OK();
	}

	
}
