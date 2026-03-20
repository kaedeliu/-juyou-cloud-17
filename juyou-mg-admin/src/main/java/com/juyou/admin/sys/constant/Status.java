package com.juyou.admin.sys.constant;

/**
 * 状态枚举
 * 
 * @author kaedeliu
 *
 */
public enum Status {

	禁用(0, "禁用"), 启用(1, "启用");

	private Integer id;

	private String value;

	Status(Integer id, String value) {
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
