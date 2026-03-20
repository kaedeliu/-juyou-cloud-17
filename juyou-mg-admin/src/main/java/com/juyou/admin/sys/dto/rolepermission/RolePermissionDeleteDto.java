package com.juyou.admin.sys.dto.rolepermission;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色菜单删除对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色菜单删除对象", description = "角色菜单删除对象")
public class RolePermissionDeleteDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "角色ID")
	private List<String> roleIds;

	@Schema(description = "菜单ID")
	private List<String> permissionIds;
}
