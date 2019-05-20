package com.datasource.entity;

public enum DataSourceEnum {

	// 主表
	MASTER("master"),
	// 从表
	SLAVE("slave");

	private String name;

	private DataSourceEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
