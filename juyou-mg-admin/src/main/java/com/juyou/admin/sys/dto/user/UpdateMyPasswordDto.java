package com.juyou.admin.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 修改密码对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "修改密码对象", description = "修改密码对象")
public class UpdateMyPasswordDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "原密码")
	@NotBlank(message = "原密码不能为空")
	private String oldPassword;

	@Schema(description = "新密码")
	@NotBlank(message = "新密码不能为空")
	private String newPassword;
}
