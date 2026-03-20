package com.juyou.admin.sys.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 角色修改对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色修改对象", description = "角色修改对象")
public class RoleEditDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键", required = true)
	@NotBlank(message = "主键不能为空")
	private String roleId;

	@Schema(description = "角色名称")
	@NotBlank(message = "角色名称不能为空")
	private String name;

	@Schema(description = "角色编号admin,user")
	@NotBlank(message = "角色编号不能为空")
	private String code;

	@Schema(description = "状态")
	@NotNull(message = "角色状态不能为空")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
