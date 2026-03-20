package com.juyou.admin.sys.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "用户拥有的权限")
public class UserPermissionAllDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "菜单权限")
	List<UserPermissionDto> userPermissionDtos;

	@Schema(description = "按钮权限")
	List<UserBtnPermissionDto> userBtnPermissionDtos;
}
