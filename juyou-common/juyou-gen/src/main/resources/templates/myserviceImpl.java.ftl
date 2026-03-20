package ${package.ServiceImpl};

import java.util.List;

import ${package.Parent}.dto.${entity?lower_case}.${entity}PageDto;
import com.juyou.common.utils.PageUtils;
import ${package.Parent}.dto.${entity?lower_case}.${entity}AddDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}EditDto;
import ${package.Parent}.dto.${entity?lower_case}.${entity}ListDto;
import com.juyou.common.dto.IdDto;
import com.juyou.common.dto.IdsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juyou.common.query.QueryGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import java.util.Arrays;

<#if customTable[table.name]??>
      <#assign customTableData=customTable[table.name]/>
</#if>

@Service
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

   @Override
   @Transactional(rollbackFor = Exception.class)
   public IPage<${entity}ListDto> pageList(${entity}PageDto ${table.entityPath}PageDto) throws  Exception{
        QueryWrapper<${entity}> queryWrapper = QueryGenerator.initQueryWrapper(${table.entityPath}PageDto,${entity}.class);
        queryWrapper.select(<#list table.fields as field><#if customTableData?? && customTableData[field.annotationColumnName]??> <#assign customField=customTableData[field.annotationColumnName]/><#else><#assign customField=""/> </#if><#if customField?? && customField!="" && customField.listColumns?? && customField.listColumns==1>"${field.annotationColumnName}",</#if></#list>);
        Page<${entity}> page = new Page<${entity}>(${table.entityPath}PageDto.getPageNo(), ${table.entityPath}PageDto.getPageSize());
        IPage<${entity}> pageList = page(page, queryWrapper);
        return PageUtils.toPege(pageList,${entity}ListDto.class);
   }



   @Override
   @Transactional(rollbackFor = Exception.class)
   public void insert( ${entity}AddDto ${table.entityPath}AddDto){
        ${entity} ${table.entityPath} = new ${entity}();
        BeanUtils.copyProperties(${table.entityPath}AddDto,${table.entityPath});
        save(${table.entityPath});
   }


   @Override
   @Transactional(rollbackFor = Exception.class)
   public void update(${entity}EditDto ${table.entityPath}EditDto){
        ${entity} ${table.entityPath} = new ${entity}();
        BeanUtils.copyProperties(${table.entityPath}EditDto,${table.entityPath});
        updateById(${table.entityPath});
   }

   @Override
   @Transactional(rollbackFor = Exception.class)
   public void delete(IdDto dto){
        removeById(dto.getId());
   }

   @Override
   @Transactional(rollbackFor = Exception.class)
   public void deletes( IdsDto dto){
        removeByIds(Arrays.asList(dto.getIds()));
   }
}