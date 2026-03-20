package com.juyou.admin.sys.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 菜单添加对象
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单添加对象", description = "菜单添加对象")
public class PermissionAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "编码", required = true)
	@NotBlank(message = "编码不能为空")
	private String code;

	@Schema(description = "名称", required = true)
	@NotBlank(message = "名称不能为空")
	private String name;

	@Schema(description = "父ID")
	private String parentId;

	@Schema(description = "状态")
	private Integer status;

	@Schema(title = "路径", required = true)
	@NotBlank(message = "路径不能为空")
	private String url;

	@Schema(description = "权限标识")
	private String authUrl;

	@Schema(title = "前端组件名称", required = true)
	@NotBlank(message = "前端组件名称不能为空")
	private String componentName;

	@Schema(description = "前端组件路径", required = true)
	@NotBlank(message = "前端组件路径不能为空")
	private String componentUrl;

	@Schema(description = "0:一级菜单;1:子菜单:2:按钮权限,默认0")
	private Integer type;

	@Schema(description = "菜单图标")
	private String icon;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "排序号")
	private Integer sequence;
}
