package com.juyou.admin.sys.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.admin.sys.dto.dict.DictAddDto;
import com.juyou.admin.sys.entity.Dict;
import com.juyou.admin.sys.service.DictService;
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
@Tag(name = "系统字典")
@RequestMapping("/sys/dict")
public class DictController {

	@Resource
	private DictService dictService;

	@Operation(summary = "系统字典-列表", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统字典-列表", db = false)
	@PostMapping("/pageList")
	@Tenants
	public Result<IPage<Dict>> pageList(@RequestBody @Validated com.juyou.admin.sys.dto.dict.DictPageDto dictPage) throws Exception {
		IPage<Dict> pageList = this.dictService.pageList(dictPage);
		return new Result<>(pageList);
	}

	@Operation(summary = "系统字典-详情", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统字典-详情", db = false)
	@PostMapping("/findById")
	public Result<Dict> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(dictService.findById(dto));
	}

	@Operation(summary = "系统字典-添加", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "系统字典-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody DictAddDto dictAddDto) {
		this.dictService.insert(dictAddDto);
		return Result.OK();
	}

	@Operation(summary = "系统字典-修改", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "系统字典-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody com.juyou.admin.sys.dto.dict.DictEditDto dictEditDto) {
		this.dictService.update(dictEditDto);
		return Result.OK();
	}

	@Operation(summary = "系统字典-删除", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统字典-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.dictService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "系统字典-批量删除", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统字典-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.dictService.deletes(dto);
		return Result.OK();
	}

	@Operation(summary = "-所有列表", tags = { "系统字典" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统字典-所有列表", db = false)
	@PostMapping("/list")
	@Tenants
	public Result<com.juyou.admin.sys.dto.dict.DictVersionDto> list(@RequestBody @Validated com.juyou.admin.sys.dto.dict.DictListDto dictListDto) throws Exception {
		com.juyou.admin.sys.dto.dict.DictVersionDto dictVersionDto = this.dictService.list(dictListDto);
		return new Result<>(dictVersionDto);
	}

}
