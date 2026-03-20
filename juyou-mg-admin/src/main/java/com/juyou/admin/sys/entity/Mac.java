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

import java.util.List;

/**
 * 机构
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_mac")
@Schema(title = "机构对象", description = "机构对象")
public class Mac extends BaseEntity {

	private static final long serialVersionUID = 1985004109407387960L;

	@Schema(description = "主键")
	@TableId(value = "mac_id", type = IdType.INPUT)
	private String macId;

	@Schema(description = "父机构主键")
	@TableField("parent_id")
	private String parentId;

	@Schema(description = "编码")
	@TableField("code")
	private String code;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(description = "备注")
	@TableField("remark")
	private String remark;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "机构类型")
	@TableField("type")
	private Integer type;

	@Schema(description = "排序号")
	@TableField("sequence")
	private Integer sequence;


	@TableField(exist = false)
	@Schema(description = "子机构")
	private List<Mac> children;
}
