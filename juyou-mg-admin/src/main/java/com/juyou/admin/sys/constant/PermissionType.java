package com.juyou.admin.sys.constant;

/**
 * 系统菜单枚举
 * 
 * @author kaedeliu
 *
 */
public enum PermissionType {

	一级菜单(0, "一级菜单"), 子菜单(1, "子菜单"), 按钮权限(2, "按钮权限");

	private Integer id;

	private String value;

	PermissionType(Integer id, String value) {
		this.id = id;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
