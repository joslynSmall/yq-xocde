package com.yq.xcode.security.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

import lombok.Data;
@Data
public class PermissionCriteria extends HPageCriteria  {
	/**
	 * 代码
	 */
	@ParameterLogic(colName = "a.CODE", operation="like" )
	private String code;

	/**
	 * 描述
	 */
	@ParameterLogic(colName = "a.DESCRIPTION", operation="like" )
	private String description;
	
	/**
	 * 名称
	 */
	@ParameterLogic(colName = "a.NAME", operation="like" )
	private String name;


}
