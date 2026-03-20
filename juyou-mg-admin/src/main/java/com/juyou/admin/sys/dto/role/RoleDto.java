package com.juyou.admin.sys.dto.role;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色DTO
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色DTO", description = "角色DTO")
public class RoleDto implements Serializable {

	private static final long serialVersionUID = 3903674277984537240L;

	@Schema(description = "主键")
	private String roleId;

	@Schema(description = "角色名称")
	private String name;

	@Schema(description = "角色编号")
	private String code;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "菜单ID")
	private List<String> permissionIds;
}
