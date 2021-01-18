package com.yq.xcode.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 用来处理审批查询的扩展字段， 包括关联的表和条件
 * @author jettie
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkFlowEntitySkipPageView extends IdAndVersion {
	private String entityNumber;
	private String statusDsp;
	private String entityCategory;
	private Long entityTemplateId; 
	
	/**
	 * 扩展字段移到10
	 */
 // 如果需要原因， 在这个页面输入， 保存在action_log 表一下
	private String actionReason;
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public String getStatusDsp() {
		return statusDsp;
	}
	public void setStatusDsp(String statusDsp) {
		this.statusDsp = statusDsp;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
	public Long getEntityTemplateId() {
		return entityTemplateId;
	}
	public void setEntityTemplateId(Long entityTemplateId) {
		this.entityTemplateId = entityTemplateId;
	}
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	public String getCol4() {
		return col4;
	}
	public void setCol4(String col4) {
		this.col4 = col4;
	}
	public String getCol5() {
		return col5;
	}
	public void setCol5(String col5) {
		this.col5 = col5;
	}
	public String getCol6() {
		return col6;
	}
	public void setCol6(String col6) {
		this.col6 = col6;
	}
	public String getCol7() {
		return col7;
	}
	public void setCol7(String col7) {
		this.col7 = col7;
	}
	public String getCol8() {
		return col8;
	}
	public void setCol8(String col8) {
		this.col8 = col8;
	}
	public String getCol9() {
		return col9;
	}
	public void setCol9(String col9) {
		this.col9 = col9;
	}
	public String getCol10() {
		return col10;
	}
	public void setCol10(String col10) {
		this.col10 = col10;
	}
	public String getActionReason() {
		return actionReason;
	}
	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
 
 
}

