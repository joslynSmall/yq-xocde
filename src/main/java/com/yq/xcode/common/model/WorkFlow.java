package com.yq.xcode.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.common.bean.ParseElementUse;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow")
public class WorkFlow extends YqJpaBaseModel{

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "WORK_FLOW_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	
	/**
	 * 代码
	 */
	@Column(name = "WORK_FLOW_NUMBER", length = 20, nullable = false)
	private String workFlowNumber;

	/**
	 * 名称
	 */
	@Column(name = "WORK_FLOW_NAME", length = 80, nullable = false)
	private String workFlowName;
	
	/**
	 * 类型ID, 是何种类型的审批， 不同种类型的审批可用的条件不同
	 */
	@Column(name = "CATEGORY_CODE", length = 20, nullable = false)
	private String categoryCode;
	
	/**
	 * 工作流条件, 同一实体， 是否可以用改工作流
	 */
	@Column(name = "USE_RELATION_FUNCTION", length = 800, nullable = false)
	private String useRelationFunction;
	/**
	 * 流程开始使用日期
	 */
	@Column(name = "START_DATE", length = 800, nullable = false)
	private Date startDate;
	/**
	 * 流程结束使用日期
	 */
	@Column(name = "END_DATE", length = 800, nullable = false)
	private Date endDate;
	
	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION", length = 100)
	private String description;
	
	
	/**
	 * 权重, 小的优先
	 */
	@Column(name = "line_Number", length = 100)
	private Integer lineNumber;
	
	

	@Transient
	private String categoryName;
	
	@Transient
	private ParseElementUse  parseElementUse;
	

 	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getWorkFlowNumber() {
		return workFlowNumber;
	}

	public void setWorkFlowNumber(String workFlowNumber) {
		this.workFlowNumber = workFlowNumber;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}




	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}



	public String getUseRelationFunction() {
		return useRelationFunction;
	}

	public void setUseRelationFunction(String useRelationFunction) {
		this.useRelationFunction = useRelationFunction;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Transient
	private String useRelationFunctionStr;
	public String getUseRelationFunctionStr() {
		return useRelationFunctionStr;
	}
	
	public void setUseRelationFunctionStr(String useRelationFunctionStr) {
		this.useRelationFunctionStr = useRelationFunctionStr;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public ParseElementUse getParseElementUse() {
		return parseElementUse;
	}

	public void setParseElementUse(ParseElementUse parseElementUse) {
		this.parseElementUse = parseElementUse;
	}
	
 
	
}
