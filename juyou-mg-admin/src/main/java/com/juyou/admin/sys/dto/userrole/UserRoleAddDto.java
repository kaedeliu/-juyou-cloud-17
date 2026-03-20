package com.juyou.admin.sys.dto.userrole;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户角色添加对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户角色添加对象", description = "用户角色添加对象")
public class UserRoleAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "用户ID")
	@NotBlank(message = "用户ID不能为空")
	private String userId;

	@Schema(description = "角色ID")
	@NotEmpty(message = "角色ID不能为空")
	private List<String> roleIds;
}
