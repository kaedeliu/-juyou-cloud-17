package com.juyou.admin.sys.dto.role;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 角色添加对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色添加对象", description = "角色添加对象")
public class RoleAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "角色名称", required = true)
	@NotBlank(message = "名称不能为空")
	private String name;

	@Schema(description = "角色编号admin,user", required = true)
	@NotBlank(message = "编码不能为空")
	private String code;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "备注")
	private String remark;
}
