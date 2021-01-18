package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.CriteriaCol;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "pur_WorkFlow_MutiOperation")

@EntityDesc(name = "批量审批", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkFlowMutiOperation extends YqJpaBaseModel {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@CriteriaCol
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3)
	@ColumnLable(name = "编码")
	@Column(name = "lookupCode_Key")
	private String lookupCodeKey;
	
	@CriteriaCol
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3)
	@ColumnLable(name = "审批类型")
	@Column(name = "categoryCode")
	private String categoryCode;

	@Transient
	private WorkFlowDetail workFlowDetail;
	@Transient
	private WorkFlow workFlow;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getLookupCodeKey() {
		return lookupCodeKey;
	}

	public void setLookupCodeKey(String lookupCodeKey) {
		this.lookupCodeKey = lookupCodeKey;
	}

	public WorkFlowDetail getWorkFlowDetail() {
		return workFlowDetail;
	}

	public void setWorkFlowDetail(WorkFlowDetail workFlowDetail) {
		this.workFlowDetail = workFlowDetail;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public WorkFlow getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}

	
	
	
	
	
	
}