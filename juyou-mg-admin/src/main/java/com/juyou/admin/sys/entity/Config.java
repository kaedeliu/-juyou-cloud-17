package com.juyou.admin.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.juyou.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统参数配置
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_config")
@Schema(title = "系统参数对象", description = "系统参数对象")
public class Config extends BaseEntity {

	private static final long serialVersionUID = -9018959007599240858L;

	@Schema(description = "系统参数主键")
	@TableId(value = "config_id", type = IdType.ASSIGN_ID)
	private String configId;

	@Schema(description = "编码")
	@TableField("code")
	private String code;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(description = "值")
	@TableField("value")
	private String value;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "排序号")
	@TableField("sequence")
	private Integer sequence;

	@Schema(description = "备注")
	@TableField("remark")
	private String remark;


	@Schema(description = "系统类型")
	@TableField("sys_type")
	private String sysType;
}
