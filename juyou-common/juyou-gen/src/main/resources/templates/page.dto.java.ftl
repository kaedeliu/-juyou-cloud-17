package ${genConfig.parent}.${genConfig.moduleName}.dto.${entity?lower_case};
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
import com.juyou.common.dto.PageDto;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

<#if swagger>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
    <#if chainModel>
import lombok.experimental.Accessors;
    </#if>
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
    <#if chainModel>
@Accessors(chain = true)
    </#if>
</#if>
<#if swagger>
@Schema(title="${entity}PageDto对象", description="${entity}Page查询对象")
</#if>
public class ${entity}PageDto extends PageDto {

<#if entitySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#if customTable[table.name]??>
      <#assign customTableData=customTable[table.name]/>
</#if>

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if customTableData?? && customTableData[field.annotationColumnName]??>
         <#assign customField=customTableData[field.annotationColumnName]/>
    <#else>
         <#assign customField=""/>
    </#if>
    <#-- 需要查询的字段才生成到Page里面-->
    <#if customField?? && customField!="" && customField.queryColumns?? && customField.queryColumns==1>
        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
        </#if>

        <#if customField.fieldDesc!?length gt 0>
            <#if swagger>
        @Schema(description = "${customField.fieldDesc!""}")
            <#else>
        /**
         * ${customField.fieldDesc!""}
         */
            </#if>
        </#if>
        <#-- 乐观锁注解 -->
        <#if field.versionField>
        @Version
        </#if>
        <#-- 逻辑删除注解 -->
        <#if field.logicDeleteField>
        @TableLogic
        </#if>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
}