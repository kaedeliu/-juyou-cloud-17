package com.juyou.gen.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
@TableName("juyou_sys_table_info")
@Schema(title="TableInfo对象", description="")
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "表名")
    @TableId("table_name")
    private String tableName;

    @Schema(description = "字段名")
    @TableField("column_name")
    private String columnName;

    @TableField("java_type")
    private String javaType;

    @Schema(description = "java字段名，不填写为小驼峰")
    @TableField("java_name")
    private String javaName;

    @Schema(description = "新增")
    @TableField("add_columns")
    private Integer addColumns;

    @Schema(description = "编辑")
    @TableField("edit_columns")
    private Integer editColumns;

    @Schema(description = "列表")
    @TableField("list_columns")
    private Integer listColumns;

    @Schema(description = "查询")
    @TableField("query_columns")
    private Integer queryColumns;

    @Schema(description = "查询方式")
    @TableField("query_type")
    private Integer queryType;

    @Schema(description = "必填")
    @TableField("not_null_columns")
    private Integer notNullColumns;

    @Schema(description = "输入框类型")
    @TableField("out_type")
    private Integer outType;

    @Schema(description = "字典类型")
    @TableField("ditc_type")
    private String ditcType;

    @Schema(description = "正则表达式")
    @TableField("reg_exp")
    private String regExp;

    @Schema(description = "正则验证消息")
    @TableField("reg_msg")
    private String regMsg;

    @Schema(description = "上次生成moduleName")
    @TableField("module_name")
    private String moduleName;


    @Schema(description = "字段说明,用于生成前端")
    @TableField("field_desc")
    private String fieldDesc;

}