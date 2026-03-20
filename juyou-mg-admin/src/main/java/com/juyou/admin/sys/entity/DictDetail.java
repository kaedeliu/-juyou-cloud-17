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
 * 系统字典明细
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_dict_detail")
@Schema(title = "系统字典明细对象", description = "系统字典明细对象")
public class DictDetail extends BaseEntity {

	private static final long serialVersionUID = -6287766837338370898L;

	@Schema(description = "系统字典明细主键")
	@TableId(value = "dict_detail_id", type = IdType.ASSIGN_ID)
	private String dictDetailId;

	@Schema(description = "系统字典主键")
	@TableField(value = "dict_id")
	private String dictId;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(title = "值", name = "值", description = "值")
	@TableField("value")
	private String value;

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
