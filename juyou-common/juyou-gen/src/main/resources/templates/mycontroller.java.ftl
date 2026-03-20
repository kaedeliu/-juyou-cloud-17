package ${package.Controller};

import java.util.Arrays;

import ${package.Parent}.dto.${entity?lower_case}.${entity}PageDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}AddDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}EditDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}ListDto;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import lombok.RequiredArgsConstructor;
import com.juyou.common.constant.CommonConstant;
import com.juyou.common.query.QueryGenerator;
import com.juyou.common.log.AutoLog;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import com.juyou.common.utils.Result;
import ${package.Entity}.${entity};
import ${package.Service}.${table.serviceName};

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if restControllerStyle>
@RestController
<#else>
@Controller
@Tag(name = "XXXX模块名称")
</#if>
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
@RequiredArgsConstructor(onConstructor = @_(@Autowired))
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
	@Autowired
	${table.serviceName} ${table.entityPath}Service;
	
	@Operation(summary = "XXXX模块名称-列表", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "XXXX模块名称-${table.comment!}-列表", db = false)
	@PostMapping("/pageList")
	//@Tenants //多租户
	public Result<IPage<${entity}ListDto>> pageList(@Validated @RequestBody ${entity}PageDto ${table.entityPath}PageDto) throws  Exception{
        IPage<${entity}ListDto> pageList = ${table.entityPath}Service.pageList(${table.entityPath}PageDto);
		return new Result<>(pageList);
	}
	
	
	@Operation(summary = "XXXX模块名称-详情", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_查询_1, value = "XXXX模块名称-${table.comment!}详情", db = false)
	@PostMapping("/findById")
	public Result<${entity}> findById(@Validated @RequestBody IdDto dto){
		return Result.OK(${table.entityPath}Service.getById(dto.getId()));
	}


	@Operation(summary = "XXXX模块名称-添加", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_添加_2, value = "XXXX模块名称-${table.comment!}-添加", db = false)
	@PostMapping("/insert")
	public Result<Object> insert(@Validated @RequestBody ${entity}AddDto ${table.entityPath}AddDto){
		${table.entityPath}Service.insert(${table.entityPath}AddDto);
		return Result.OK();
	}
	
	@Operation(summary = "XXXX模块名称-修改", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_更新_3, value = "XXXX模块名称-${table.comment!}修改", db = false)
	@PostMapping("/update")
	public Result<Object> update(@Validated @RequestBody ${entity}EditDto ${table.entityPath}EditDto){
		${table.entityPath}Service.update(${table.entityPath}EditDto);
		return Result.OK();
	}
	
	@Operation(summary = "XXXX模块名称-删除", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "XXXX模块名称-${table.comment!}-删除", db = false)
	@PostMapping("/delete")
	public Result<Object> delete(@Validated @RequestBody IdDto dto){
		${table.entityPath}Service.delete(dto);
		return Result.OK();
	}

	@Operation(summary = "XXXX模块名称-批量删除", tags = {"XXXX模块名称"})
	@AutoLog(operateType = CommonConstant.OPERATE_删除_4, value = "XXXX模块名称-${table.comment!}-批量删除", db = false)
	@PostMapping("/deletes")
	public Result<Object> deletes(@Validated @RequestBody IdsDto dto){
		${table.entityPath}Service.removeByIds(Arrays.asList(dto.getIds()));
		return Result.OK();
	}

	

	
}
</#if>