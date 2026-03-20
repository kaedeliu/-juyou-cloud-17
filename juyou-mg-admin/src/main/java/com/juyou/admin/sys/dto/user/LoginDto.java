package com.juyou.admin.sys.dto.user;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 登录对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "登录对象", description = "登录对象")
public class LoginDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "登录名")
	@NotBlank(message = "登录名不能为空")
	private String loginName;

	@Schema(description = "密码")
	@NotBlank(message = "密码不能为空")
	private String password;

	/**
	 * 登录IP
	 */
	@Schema(hidden = true)
	private String ip;

}
