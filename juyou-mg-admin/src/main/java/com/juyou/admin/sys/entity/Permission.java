package com.juyou.admin.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * 
 * @author kaedeliu
 *
 */
@Data
@Accessors(chain = true)
@TableName("juyou_sys_permission")
@Schema(title = "菜单对象", description = "菜单对象")
public class Permission implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "系统菜单主键")
	@TableId(value = "permission_id", type = IdType.ASSIGN_ID)
	private String permissionId;

	@Schema(description = "编码")
	@TableField("code")
	private String code;

	@Schema(description = "名称")
	@TableField("name")
	private String name;

	@Schema(description = "状态")
	@TableField("status")
	private Integer status;

	@Schema(description = "父ID")
	@TableField("parent_id")
	private String parentId;

	@Schema(description = "路径")
	@TableField("url")
	private String url;

	@Schema(description = "权限标识")
	@TableField("auth_url")
	private String authUrl;

	@Schema(description = "前端组件名称")
	@TableField("component_name")
	private String componentName;

	@Schema(description = "前端组件路径")
	@TableField("component_url")
	private String componentUrl;

	@Schema(description = "0:一级菜单;1:子菜单:2:按钮权限")
	@TableField("type")
	private Integer type;

	@Schema(description = "菜单图标")
	@TableField("icon")
	private String icon;

	@Schema(description = "描述")
	@TableField("description")
	private String description;

	@Schema(description = "排序号")
	@TableField("sequence")
	private Integer sequence;

	@Schema(description = "创建时间")
	@TableField("create_time")
	private Date createTime;

	@Schema(description = "创建人")
	@TableField("create_name")
	private String createName;

	@Schema(description = "最后修改时间")
	@TableField(value = "last_update_time")
	private Date lastUpdateTime;

	@Schema(description = "最后修改人")
	@TableField(value = "last_update_name")
	private String lastUpdateName;

	@Schema(description = "类型，0默认,-1超管")
	@TableField("sys_type")
	private Integer sysType;

	@TableField(exist = false)
	@Schema(description = "子菜单")
	private List<Permission> children;
}
