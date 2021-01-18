package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_PARSE_ELEMENT_DISPLAY")

public class ParseElementDisplay extends YqJpaBaseModel {

	@Id
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 一般是数据的
	 */
	@ColumnLable(name = "来源ID")
	@Column(name = "model_id")
	private Long modelId;
	
	/**
	 * 来源明细ID, 多数用receivableDtlFun.id
	 */
	@ColumnLable(name = "来源ID")
	@Column(name = "model_dtl_id")
	private Long modelDtlId;
	
 	/**
	 * modelproperty - 模型加属性ID
	 */
	@ColumnLable(name = "模型属性")
	@Column(name = "model_property")
	private String modelProperty;
	
	@ColumnLable(name = "编码")
	@Column(name = "fun_code")
	private String funCode;
	
	@ColumnLable(name = "值")
	@Column(name = "value")
	private String value;
	
	@ColumnLable(name = "值")
	@Column(name = "display_express")
	private String displayExpress;
	
	@Transient
	private String funName;
	
	
	public String getFunName() {
		return funName;
	}
	public void setFunName(String funName) {
		this.funName = funName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public String getModelProperty() {
		return modelProperty;
	}
	public void setModelProperty(String modelProperty) {
		this.modelProperty = modelProperty;
	}
	public String getFunCode() {
		return funCode;
	}
	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplayExpress() {
		return displayExpress;
	}
	public void setDisplayExpress(String displayExpress) {
		this.displayExpress = displayExpress;
	}
	
 	public Long getModelDtlId() {
		return modelDtlId;
	}
	public void setModelDtlId(Long modelDtlId) {
		this.modelDtlId = modelDtlId;
	}
	
	public String getDisplayExpressDsp() {
		if (CommonUtil.isNull(this.displayExpress)) {
			return this.displayExpress;
		}
 		return this.displayExpress.replace("\n", "<br>").replace("   ", "&nbsp;&nbsp;");
	}
	 
}