package com.yq.xcode.common.bean;

import lombok.Data;

@Data
public class PageVo {

	/**
	 * 页号
	 */
	private int pageNumber;

	/**
	 * 页面大小
	 */
	private int pageSize;

	/**
	 * 排序字段
	 */
	private String sort = "createTime";

	/**
	 * 排序方式 ASC/DESC
	 */
	private String order = "DESC";
}