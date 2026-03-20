package com.juyou.gen.dto.tableinfo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author yx
 * @since 2023-04-08 17:05:31
 */

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Schema(title="TableInfoColumnInfoDto", description="TableInfoColumnInfoDto")
public class TableInfoColumnInfoDto {

    private static final long serialVersionUID = 1L;

    @Schema(description = "字段名",required = true)
    @NotNull(message = "请填写列名")
    private String columnName;

    @Schema(description = "新增0-是,1-否",required = true)
    @NotNull(message = "请填写是否新增")
    private Integer addColumns;

    @Schema(description = "编辑0-是,1-否",required = true)
    @NotNull
    private Integer editColumns;

    @Schema(description = "字段说明",required = true)
    @NotNull
    private String fieldDesc;

    @Schema(description = "列表0-是,1-否",required = true)
    @NotNull
    private Integer listColumns;

    @Schema(description = "查询0-是,1-否",required = true)
    @NotNull
    private Integer queryColumns;

    @Schema(description = "查询方式",required = true)
    @NotNull
    private Integer queryType;

    @Schema(description = "必填0-是,1-否",required = true)
    @NotNull
    private Integer notNullColumns;

    @Schema(description = "输入框类型0-文本,1-密码,2-数字,3下拉列表,10字典",required = true)
    @NotNull
    private Integer outType;

    @Schema(description = "字典类型")
    private String ditcType;

    @Schema(description = "正则表达式")
    private String regExp;

    @Schema(description = "正则验证消息")
    private String regMsg;

    @Schema(description = "上次生成moduleName")
    private String moduleName;
}