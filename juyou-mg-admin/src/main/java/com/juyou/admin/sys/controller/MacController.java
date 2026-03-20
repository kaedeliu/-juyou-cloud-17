package com.juyou.admin.sys.controller;

import com.juyou.admin.sys.dto.mac.MacAddDto;
import com.juyou.admin.sys.dto.mac.MacEditDto;
import com.juyou.admin.sys.dto.mac.MacTreeDto;
import com.juyou.admin.sys.entity.Mac;
import com.juyou.admin.sys.service.MacService;
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
import java.util.List;

@RestController
@Tag(name = "机构")
@RequestMapping("/sys/mac")
public class MacController {

	@Resource
	private MacService macService;

	@Operation(summary = "机构-列表", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "机构-列表", db = false)
	@PostMapping("/list")
	@Tenants
	public Result<List<Mac>> list() throws Exception {
		List<Mac> list = this.macService.list();
		return new Result<>(list);
	}

	@Operation(summary = "机构-详情", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "机构-详情", db = false)
	@PostMapping("/findById")
	public Result<Mac> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(macService.findById(dto));
	}

	@Operation(summary = "机构-添加", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "机构-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody MacAddDto macAddDto) throws IllegalAccessException {
		this.macService.insert(macAddDto);
		return Result.OK();
	}

	@Operation(summary = "机构-修改", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "机构-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody MacEditDto macEditDto) {
		this.macService.update(macEditDto);
		return Result.OK();
	}

	@Operation(summary = "机构-删除", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "机构-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.macService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "机构-批量删除", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "机构-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.macService.deletes(dto);
		return Result.OK();
	}

	@Operation(summary = "机构-树型菜单", tags = { "机构" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "机构-树型菜单", db = false)
	@PostMapping("/findTree")
	@Tenants
	public Result<List<MacTreeDto>> findTree() throws Exception {
		List<MacTreeDto> list = this.macService.findTree();
		return new Result<>(list);
	}
}
