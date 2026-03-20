package com.juyou.gen.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.utils.Result;
import com.juyou.gen.dto.tableinfo.*;
import com.juyou.gen.service.TableInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 表单管理 前端控制器
 * </p>
 *
 * @author yx
 * @since 2023-04-08 17:22:34
 */
@Controller
@Tag(name = "基础数据")
@RequestMapping("/table-info")
public class TableInfoController {
	@Autowired
	TableInfoService tableInfoService;
	
	@Operation(summary = "表单管理-列表", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "基础数据-表单管理-列表", db = false)
	@PostMapping("/pageList")
	//@Tenants //多租户
	//@RequiresPermissions("sys:config:list") //权限注解
	public Result<IPage<DbTableDto>> pageList(@Validated @RequestBody DbTablePageDto dbTablePageDto) throws  Exception{
		QueryWrapper<DbTableDto> queryWrapper = QueryGenerator.initQueryWrapper(dbTablePageDto,DbTableDto.class);
        Page<DbTableDto> page = new Page<DbTableDto>(dbTablePageDto.getPageNo(), dbTablePageDto.getPageSize());
        IPage<DbTableDto> pageList = tableInfoService.pageList(page, queryWrapper);
		return new Result<>(pageList);
	}
	
	
	@Operation(summary = "表单管理-详情", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "基础数据-表单管理详情", db = false)
	@PostMapping("/findById")
	public Result<List<TableInfoColumnDto>> findById(@Validated @RequestBody IdDto dto){
		return Result.OK(tableInfoService.getByIdInfo(dto.getId()));
	}

	@Operation(summary = "表单管理-修改", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "基础数据-表单管理修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody TableInfoSaveDto tableInfoSaveDto){
		tableInfoService.update(tableInfoSaveDto);
		return Result.OK();
	}
	
	@Operation(summary = "表单管理-生成模板", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "基础数据-表单管理-生成模板", db = false)
	@PostMapping("/createAndZip")
	public Result<Object> createAndZip(@Validated @RequestBody CreateDto createDto) throws Exception {
		String name=tableInfoService.createAndZip(createDto);
		return Result.OK(name);
	}

	@Operation(summary = "表单管理-查看模板", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "基础数据-表单管理-查看模板", db = false)
	@PostMapping("/showTableFiles")
	public Result<List<ShowTableFilesDto>> showTableFiles(@Validated @RequestBody IdDto dto) throws Exception {
		return Result.OK(tableInfoService.showTableFiles(dto.getId()));
	}

	@Operation(summary = "表单管理-下载模板", tags = {"基础数据"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "基础数据-表单管理-下载模板", db = false)
	@GetMapping("/down")
	public void down(@Schema(description = "文件路径",required = true) @RequestParam(required = true) String filePath, HttpServletResponse response) throws Exception {
		tableInfoService.down(filePath,response);
	}

}
