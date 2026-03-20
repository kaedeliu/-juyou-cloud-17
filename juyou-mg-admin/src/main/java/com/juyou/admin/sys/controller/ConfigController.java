package com.juyou.admin.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.config.ConfigAddDto;
import com.juyou.admin.sys.dto.config.ConfigEditDto;
import com.juyou.admin.sys.dto.config.ConfigPageDto;
import com.juyou.admin.sys.entity.Config;
import com.juyou.admin.sys.service.ConfigService;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
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

@RestController
@Tag(name = "系统参数")
@RequestMapping("/sys/config")
public class ConfigController {

	@Resource
	private ConfigService configService;

	@Operation(summary = "系统参数-列表", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统参数-列表", db = false)
	@PostMapping("/pageList")
	@Tenants
	public Result<IPage<Config>> pageList(@RequestBody @Validated ConfigPageDto configPage) throws Exception {
		IPage<Config> pageList = this.configService.pageList(configPage);
		return new Result<>(pageList);
	}
	
	@Operation(summary = "系统参数-详情", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统参数-详情", db = false)
	@PostMapping("/findById")
	public Result<Config> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(configService.findById(dto));
	}

	@Operation(summary = "系统参数-添加", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "系统参数-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody ConfigAddDto configAddDto) {
		this.configService.insert(configAddDto);
		return Result.OK();
	}

	@Operation(summary = "系统参数-修改", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "系统参数-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody ConfigEditDto configEditDto) {
		this.configService.update(configEditDto);
		return Result.OK();
	}

	@Operation(summary = "系统参数-删除", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统参数-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.configService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "系统参数-批量删除", tags = { "系统参数" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统参数-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.configService.deletes(dto);
		return Result.OK();
	}
}
