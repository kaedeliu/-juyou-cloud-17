package com.juyou.admin.sys.dto.user;

import com.juyou.admin.sys.entity.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "用户拥有的权限")
public class UserPermissionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "系统菜单主键", required = true)
	private String permissionId;

	@Schema(description = "编码")
	private String code;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "状态")
	private Integer status;

	@Schema(description = "父ID")
	private String parentId;

	@Schema(description = "路径")
	private String url;

	@Schema(description = "权限标识")
	private String authUrl;

	@Schema(description = "前端组件名称")
	private String componentName;

	@Schema(description = "前端组件路径")
	private String componentUrl;

	@Schema(description = "0:一级菜单;1:子菜单:2:按钮权限")
	private Integer type;

	@Schema(description = "菜单图标")
	private String icon;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "排序号")
	private Integer sequence;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "创建人")
	private String createName;

	@Schema(description = "最后修改时间")
	private Date lastUpdateTime;

	@Schema(description = "最后修改人")
	private String lastUpdateName;

	@Schema(description = "数据归属机构编码")
	private String macId;

	@Schema(description = "子菜单")
	private List<Permission> children;
}
