package com.juyou.opm.cms.controller;

import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.tenants.Tenants;
import com.juyou.common.utils.Result;
import com.juyou.opm.cms.dto.infotype.*;
import com.juyou.opm.cms.entity.InfoType;
import com.juyou.opm.cms.service.InfoTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * <p>
 * 文章分类 前端控制器
 * </p>
 *
 * @author yx
 * @since 2023-04-24 16:50:00
 */
@Controller
@Tag(name = "文章分类管理")
@RequestMapping("/cms/info-type")
public class InfoTypeController {
	@Autowired
	InfoTypeService infoTypeService;
	
	@Operation(summary = "文章分类管理-列表", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章分类管理-文章分类-列表", db = false)
	@PostMapping("/treeList")
	@Tenants //多租户
	public Result<List<InfoTypeTreeDto>> treeList() throws  Exception{
		return new Result<>(infoTypeService.treeList());
	}
	
	@Operation(summary = "文章分类管理-详情", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章分类管理-文章分类详情", db = false)
	@PostMapping("/findById")
	public Result<InfoType> findById(@Validated @RequestBody IdDto dto){
		return Result.OK(infoTypeService.getById(dto.getId()));
	}


	@Operation(summary = "文章分类管理-添加", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "文章分类管理-文章分类-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody InfoTypeAddDto infoTypeAddDto){
		infoTypeService.insert(infoTypeAddDto);
		return Result.OK();
	}
	
	@Operation(summary = "文章分类管理-修改", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "文章分类管理-文章分类修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody InfoTypeEditDto infoTypeEditDto){
		infoTypeService.update(infoTypeEditDto);
		return Result.OK();
	}
	
	@Operation(summary = "文章分类管理-删除", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "文章分类管理-文章分类-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto){
		infoTypeService.removeById(dto.getId());
		return Result.OK();
	}

	/**
	 *
	 * @param dto
	 * @return
	 */
	@Operation(summary = "文章分类管理-查询分类属性", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章分类管理-文章分类-查询分类属性", db = false)
	@PostMapping("/findInfoTypeAttr")
	public Result<InfoTypeDto> findInfoTypeAttr(@Validated @RequestBody IdDto dto) throws Exception {
		return Result.OK(infoTypeService.findInfoTypeAttr(dto));
	}

	@Operation(summary = "文章分类管理-查询分类属性", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章分类管理-文章分类-查询分类属性", db = false)
	@PostMapping("/insertTypeAttr")
	public Result<Object> insertTypeAttr(@Validated @RequestBody InfoTypeAttrAddDto infoTypeAttrAddDto) throws Exception {
		infoTypeService.insertTypeAttr(infoTypeAttrAddDto);
		return Result.OK();
	}

	@Operation(summary = "文章分类管理-下拉列表", tags = {"文章分类管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章分类管理-文章分类-下拉列表", db = false)
	@PostMapping("/treeSelect")
	@Tenants //多租户
	public Result<List<InfoTypeTreeSelectDto>> treeSelect() throws Exception {
		return Result.OK(infoTypeService.treeSelect());
	}
}
