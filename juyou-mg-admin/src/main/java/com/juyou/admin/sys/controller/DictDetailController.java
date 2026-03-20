package com.juyou.admin.sys.controller;

import com.juyou.admin.sys.dto.dictdetail.DictDetailAddDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailEditDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailMoveDto;
import com.juyou.admin.sys.dto.dictdetail.DictDetailQueryDto;
import com.juyou.admin.sys.entity.DictDetail;
import com.juyou.admin.sys.service.DictDetailService;
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
@Tag(name = "系统字典明细")
@RequestMapping("/sys/dictDetail")
public class DictDetailController {

	@Resource
	private DictDetailService dictDetailService;

	@Operation(summary = "系统字典明细-列表", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统字典明细-列表", db = false)
	@PostMapping("/list")
	public Result<List<DictDetail>> pageList(@RequestBody @Validated DictDetailQueryDto dictDetailQueryDto) throws Exception {
		List<DictDetail> list = this.dictDetailService.list(dictDetailQueryDto);
		return new Result<>(list);
	}

	@Operation(summary = "系统字典明细-详情", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "系统字典明细-详情", db = false)
	@PostMapping("/findById")
	public Result<DictDetail> findById(@Validated @RequestBody IdDto dto) {
		return Result.OK(dictDetailService.findById(dto));
	}

	@Operation(summary = "系统字典明细-添加", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "系统字典明细-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody DictDetailAddDto dictDetailAddDto) {
		this.dictDetailService.insert(dictDetailAddDto);
		return Result.OK();
	}

	@Operation(summary = "系统字典明细-修改", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "系统字典明细-修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody DictDetailEditDto dictDetailEditDto) {
		this.dictDetailService.update(dictDetailEditDto);
		return Result.OK();
	}

	@Operation(summary = "系统字典明细-删除", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统字典明细-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto) {
		this.dictDetailService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "系统字典明细-批量删除", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "系统字典明细-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto) {
		this.dictDetailService.deletes(dto);
		return Result.OK();
	}

	@Operation(summary = "系统字典明细-移动字典项", tags = { "系统字典明细" })
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "系统字典明细-移动字典项", db = false)
	@PostMapping("/move")
	public Result<Object> move(@Validated @RequestBody DictDetailMoveDto dictDetailMoveDto) {
		this.dictDetailService.move(dictDetailMoveDto);
		return Result.OK();
	}
}
