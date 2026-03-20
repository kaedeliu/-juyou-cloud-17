package com.juyou.opm.cms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import com.juyou.common.log.AutoLog;
import com.juyou.common.tenants.Tenants;
import com.juyou.common.utils.Result;
import com.juyou.opm.cms.dto.info.*;
import com.juyou.opm.cms.dto.typeattributes.TypeAttributesAddDto;
import com.juyou.opm.cms.service.InfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 文章-属性表 前端控制器
 * </p>
 *
 * @author yx
 * @since 2023-04-24 18:47:53
 */
@Controller
@Tag(name = "文章管理")
@RequestMapping("/cms/info")
public class InfoController {
	@Autowired
	InfoService infoService;
	
	@Operation(summary = "文章管理-列表", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章管理-文章-属性表-列表", db = false)
	@PostMapping("/pageList")
	@Tenants //多租户
	public Result<IPage<InfoListDto>> pageList(@Validated @RequestBody InfoPageDto infoPageDto) throws  Exception{
        IPage<InfoListDto> pageList = infoService.pageList(infoPageDto);
		return new Result<>(pageList);
	}
	
	
	@Operation(summary = "文章管理-详情", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章管理-文章-属性表详情", db = false)
	@PostMapping("/findById")
	public Result<InfoDto> findById(@Validated @RequestBody IdDto dto) throws Exception {
		return Result.OK(infoService.findById(dto));
	}


	@Operation(summary = "文章管理-添加", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "文章管理-文章-属性表-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody InfoAddDto infoAddDto){
		infoService.insert(infoAddDto);
		return Result.OK();
	}
	
	@Operation(summary = "文章管理-修改", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "文章管理-文章-属性表修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody InfoEditDto infoEditDto){
		infoService.update(infoEditDto);
		return Result.OK();
	}
	
	@Operation(summary = "文章管理-删除", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "文章管理-文章-属性表-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto){
		infoService.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "文章管理-批量删除", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "文章管理-文章-属性表-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto){
		infoService.removeByIds(Arrays.asList(dto.getIds()));
		return Result.OK();
	}

	@Operation(summary = "文章管理-获取分类属性", tags = {"文章管理"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "文章管理-文章-属性表-获取分类属性", db = false)
	@PostMapping("/findTypeAttr")
	public Result<List<TypeAttributesAddDto>> findTypeAttr(@Validated @RequestBody IdDto dto) throws Exception {

		return Result.OK(infoService.findTypeAttr(dto));
	}

	
}
