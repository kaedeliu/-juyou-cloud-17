package com.juyou.admin.sys.dto.rolepermission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 角色菜单添加对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色菜单添加对象", description = "角色菜单添加对象")
public class RolePermissionAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "角色ID")
	@NotBlank(message = "角色ID不能为空")
	private String roleId;

	@Schema(description = "菜单ID")
	private List<String> permissionIds;
}
