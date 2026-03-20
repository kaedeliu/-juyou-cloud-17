package com.juyou.admin.sys.dto.user;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(description = "用户拥有的角色权限")
public class UserBtnPermissionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "权限标识")
	private String authUrl;

}
