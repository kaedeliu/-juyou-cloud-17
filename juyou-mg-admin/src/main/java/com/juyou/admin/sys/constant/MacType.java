package com.juyou.admin.sys.constant;

public enum MacType {
	机构(0, "一级菜单"), 部分(1, "子菜单"), 岗位(2, "按钮权限");

	private Integer id;

	private String value;

	MacType(Integer id, String value) {
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
