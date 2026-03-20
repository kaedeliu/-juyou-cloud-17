package com.juyou.admin.sys.dto.userrole;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户角色删除对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户角色删除对象", description = "用户角色删除对象")
public class UserRoleDeleteDto implements Serializable {

	private static final long serialVersionUID = 7378887657367572387L;

	@Schema(description = "用户ID")
	private List<String> userIds;

	@Schema(description = "角色ID")
	private List<String> roleIds;
}
