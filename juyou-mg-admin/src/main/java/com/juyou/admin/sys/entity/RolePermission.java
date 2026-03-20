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
 * 角色菜单
 * 
 * @author kaedeliu
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("juyou_sys_role_permission")
@Schema(title = "角色菜单", description = "角色菜单")
public class RolePermission extends BaseEntity {

	private static final long serialVersionUID = 1937431565475812271L;

	@Schema(description = "用户角色主键")
	@TableId(value = "role_permission_id", type = IdType.ASSIGN_ID)
	private String rolePermissionId;

	@Schema(description = "用户主键")
	@TableField(value = "permission_id")
	private String permissionId;

	@Schema(description = "角色主键")
	@TableField(value = "role_id")
	private String roleId;

}
