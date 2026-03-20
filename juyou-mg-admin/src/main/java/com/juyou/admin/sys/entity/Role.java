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
 * 角色
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_role")
@Schema(title = "角色对象", description = "角色对象")
public class Role extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@TableId(value = "role_id", type = IdType.ASSIGN_ID)
	private String roleId;

	@Schema(description = "角色名称")
	@TableField(value = "name")
	private String name;

	@Schema(description = "角色编号admin,user")
	@TableField(value = "code")
	private String code;

	@Schema(description = "状态")
	@TableField(value = "status")
	private Integer status;

	@Schema(description = "备注")
	@TableField(value = "remark")
	private String remark;



	@Schema(description = "角色类型,0-默认,-1-系统超管,1-机构管理员")
	@TableField("sys_type")
	private Integer sysType;
}
