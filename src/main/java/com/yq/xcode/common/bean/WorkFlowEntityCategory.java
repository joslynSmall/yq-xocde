package com.yq.xcode.common.bean;

import com.yq.xcode.common.service.WorkFlowEntityService;

public class WorkFlowEntityCategory  {

	/**
	 * 代码
	 */
	private String categoryCode;

	/**
	 * 名称
	 */
	private String categoryName;
	
  	
	/**
	 * 打开审批单的URL连接 固定后面的参数是 ?id=
	 */
	private String openUrl;
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 查询名称, 注解定义表别名
	 */
	private Class criteriaClass;
	
	/**
	 * 审批实体类
	 */
	private Class entityClass;
 
 
	/**
	 * 定义会mapping到 work_flow_entity 的字段
	 * 扩展定义, json , 对应  WorkFlowEntity attribute1-10
	 * 定义会mapping到 work_flow_entity 的字段
	 * {{‘1’,‘属性名'}}
	 */
	private String[][] attributeDef;
	
	/**
	 * 微信跳转URL, 固定后面的参数是 ?id=
	 */
	private String wxUrl;

	private String criteriaName;
	
	public WorkFlowEntityCategory(){

	}
	public WorkFlowEntityCategory(String categoryCode, String categoryName, String openUrl,String wxUrl, String serviceName,Class criteriaClass, String[][] attributeDef ) {
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.openUrl = openUrl;
		this.wxUrl = wxUrl;
		this.serviceName = serviceName; 
		this.attributeDef = attributeDef; 
		this.criteriaClass = criteriaClass;
		
	}

	public String getCriteriaName() {
		return criteriaName;
	}

	public void setCriteriaName(String criteriaName) {
		this.criteriaName = criteriaName;
	}

	public String[][] getAttributeDef() {
		return attributeDef;
	}
	public void setAttributeDef(String[][] attributeDef) {
		this.attributeDef = attributeDef;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

 
 
	public String getWxUrl() {
		return wxUrl;
	}
	public void setWxUrl(String wxUrl) {
		this.wxUrl = wxUrl;
	}
	public Class getCriteriaClass() {
		return criteriaClass;
	}
	public void setCriteriaClass(Class criteriaClass) {
		this.criteriaClass = criteriaClass;
	}
	public Class getEntityClass() {
		return entityClass;
	}
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
 
}
