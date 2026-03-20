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
 * 系统字典
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_dict")
@Schema(title = "系统字典对象", description = "系统字典对象")
public class Dict extends BaseEntity {

	private static final long serialVersionUID = -9018959007599240858L;

	@Schema(description = "系统参数主键")
	@TableId(value = "dict_id", type = IdType.ASSIGN_ID)
	private String dictId;

	@Schema(description = "编码")
	@TableField("code")
	private String code;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "备注")
	@TableField("remark")
	private String remark;

	@Schema(description = "排序号")
	@TableField("sequence")
	private Integer sequence;

}
