package com.juyou.admin.sys.dto.role;

import com.juyou.common.dto.PageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色分页查询条件对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色分页查询条件对象", description = "角色分页查询条件对象")
@EqualsAndHashCode(callSuper = false)
public class RolePageDto extends PageDto implements Serializable {

	private static final long serialVersionUID = -2355843176334935301L;

	@Schema(description = "角色名称")
	private String name;

	@Schema(description = "角色编号admin,user")
	private String code;


}
