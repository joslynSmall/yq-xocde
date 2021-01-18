package com.yq.xcode.common.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.yq.xcode.common.bean.ContractFunctionView;
import com.yq.xcode.common.bean.ContractParamsView;
import com.yq.xcode.web.ui.annotation.ColumnLable;

/**
 * 公式条款
 * @author jettie
 *
 */
/**
 * @author ycwang
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "YQ_PARSE_ELEMENT_TERMS")

public class ParseElementTerms extends YqJpaBaseModel {
	
	public static final String CHARGE_TEXT = "TEXT";
	
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
	 * modelproperty - 模型加属性ID
	 */
	@ColumnLable(name = "模型属性")
	@Column(name = "model_property")
	private String modelProperty;
	
	/**
	 * 数据模型名称换成费用项
	 */
	@ColumnLable(name = "费用项")
	@Column(name = "charge_code")
	private String chargeCode;
	
 	/**
	 * 对应函数 F430 一次性费用 函数和费用相同  ，纯文本此处为TEXT
	 */
	@ColumnLable(name = "函数")
	@Column(name = "fun_code")
	private String funCode;
	
 	/**
	 * 冗余存储，便于界面显示
	 */
	@ColumnLable(name = "函数名称")
	@Column(name = "fun_name")
	private String funName;
	/**
	 * 函数表达式 F430(P243=0.1000) 此处为费用项(金额)
	 */
	@ColumnLable(name = "表达式")
	@Column(name = "express")
	private String express;
	
	@ColumnLable(name = "标题")
	@Column(name = "terms_title")
	private String termsTitle;
	
  	@ColumnLable(name = "条款")
	@Column(name = "terms")
	private String terms;

  	@Transient
  	private int index;//下标
	@Transient
  	private List<ContractParamsView> parmList;
	@Transient
	private List<ContractFunctionView> funList;
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getChargeCode() {
		return chargeCode;
	}

	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}

	public List<ContractParamsView> getParmList() {
		return parmList;
	}

	public void setParmList(List<ContractParamsView> parmList) {
		this.parmList = parmList;
	}

	public String getFunName() {
		return funName;
	}

	public void setFunName(String funName) {
		this.funName = funName;
	}

	public List<ContractFunctionView> getFunList() {
		return funList;
	}

	public void setFunList(List<ContractFunctionView> funList) {
		this.funList = funList;
	}

	public String getTermsTitle() {
		return termsTitle;
	}

	public void setTermsTitle(String termsTitle) {
		this.termsTitle = termsTitle;
	} 

}