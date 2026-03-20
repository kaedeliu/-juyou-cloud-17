package ${genConfig.parent}.${genConfig.moduleName}.dto.${entity?lower_case};
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;
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
@Schema(title="${entity}EditDto对象", description="${entity}Edit对象")
</#if>
public class ${entity}EditDto {

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
    <#-- 要新增的字段才写入-->
    <#if customField?? && customField!="" && customField.editColumns?? && customField.editColumns==1>
        <#if field.keyFlag>
            <#assign keyPropertyName="${field.propertyName}"/>
        </#if>
        <#--非空-->
        <#if customField?? && customField!="" && customField.notNullColumns?? && customField.notNullColumns==1>
            <#if field.propertyType=="String">
    @NotNull<#if customField.fieldDesc!?length gt 0>(message = "请填写${customField.fieldDesc}")</#if>
            <#else>
    @NotNull<#if customField.fieldDesc!?length gt 0>(message = "请填写${customField.fieldDesc}")</#if>
            </#if>
        </#if>
        <#--正则-->
        <#if customField?? && customField!="" && customField.regExp?? && customField.regExp!?length gt 0>
    @Pattern(regexp = "${customField.regExp}" <#if customField.regMsg??> ,message ="${customField.regMsg!""}" </#if>)
        </#if>
            <#if swagger>
        @Schema(description = "${customField.fieldDesc!""}" <#if customField?? && customField!="" && customField.notNullColumns?? && customField.notNullColumns==1>,required = true</#if>)
            <#else>
        /**
         * ${customField.fieldDesc!""}
         */
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