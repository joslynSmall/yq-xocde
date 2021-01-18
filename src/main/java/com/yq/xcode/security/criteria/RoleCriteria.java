package com.yq.xcode.security.criteria;

import com.yq.xcode.annotation.ParameterLogic;
import com.yq.xcode.common.springdata.HPageCriteria;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RoleCriteria extends HPageCriteria {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3525226274998440625L;

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
	/**
	 * 类别
	 */
	@ParameterLogic(colName = "a.TYPE", operation="=" )
	private String type;
}
