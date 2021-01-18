package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
@Table(name = "yq_work_flow_DETAIL")
public class WorkFlowDetail extends YqJpaBaseModel{

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "WORK_FLOW_DETAIL_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;
	/*
	 * 主表ID
	 */	
	@Column(name = "WORK_FLOW_ID",nullable = false)
	private Long workFlowId;
	/*
	 * 当前状态
	 */	
	@Column(name="CURRENT_STATUS",nullable=false)
	private String currentStatus;
	
	/*
	 * 当前图的状态
	 */	
	@Column(name="CURRENT_Graph_NODE",nullable=false)
	private String currentGraphNode;
	/*
	 * 当前图的状态
	 */	
	@Column(name="NEXT_Graph_NODE",nullable=false)
	private String nextGraphNode;
	
	/**
	 * 根据图的节点自动设置
	 */
	@Column(name="READ_ONLY",nullable=false)
	private boolean readOnly;
	
	/**
	 * 下一操作是否为系统
	 */
	@ColumnLable(name = "系统操作")
	@Column(name = "NEXT_IS_SYS_ACTION")
	private Boolean nextIsSysAction = false;
	
 	/*
	 * 角色	通过权限平台
	 */	
	@Column(name = "ROLE" , length=40, nullable = false)
	private String role;
	/*
	 * 操作
	 */	
	@Column(name="ACTION",nullable=false)
	private String action;
	
	
	/*
	 * 可审批条件函数
	 */	
	@Column(name = "RELATION_FUNCTION" , length=200)
	private String relationFunction;
	
	/*
	 * 不可审批提示信息
	 */	
	@Column(name = "FALSE_MESSAGE" , length=200)
	private String falseMessage;
	/*
	 * 执行函数
	 */	
	@Column(name = "EXECUTE_FUNCTION" , length=200)
	private String executeFunction;
	
	/**
	 * 下一状态
	 */
	@Column(name="NEXT_STATUS",nullable=false)
	private String nextStatus;
	
	/*
	 * 是否需要操作原因
	 */	
	@Column(name = "REASON_MANDATORY")
	private Boolean reasonMandatory;
	
 
	
  /*
	 * 操作原因TITLE
	 */	
	@Column(name = "REASON_TITLE")
	private String reasonTitle;
	
	
	  /*
	   * 成功审批完成，也就是没有下一个状态操作了， 不执行 closeEntity
	   * 主要为解决作废单据， 不能执行成功事件
	   */	
	@Column(name = "NOT_EXE_CLOSE_ENTITY")
	private Boolean notExeCloseEntity;
	
 
	/*
	 * 行号
	 */	
	@Column(name = "LINE_NUMBER", nullable=false)
	private int lineNumber;
	

	/*
	 * 描述
	 */	
	@Column(name = "DESCRIPTION" , length=100)
	private String description;

	/*
	 * 操作前提示
	 */	
	@Column(name="ACTION_PREMSG",nullable=false)
	private String actionPreMsg;
	
	/*
	 * 跳转页面
	 */	
	@Column(name="WorkFlow_SKIP_PAGE_DEFINE_ID",nullable=false)
	private Long workFlowSkipPageDefineId;
 	
	/**
	 * 当前状态名
	 */
	@Transient
	private String currentStatusName;
	/**
	 * 角色名
	 */
	@Transient
	private String roleName;
	/**
	 * 操作名
	 */
	@Transient
	private String actionName;
	/**
	 * 下一状态名
	 */
	@Transient
	private String nextStatusName;
	/**
	 * 条件函数
	 */
	@Transient
	private String relationFunctionName;
	
	/**
	 * 执行事件
	 */
	@Transient
	private String executeFunctionName;
	
	/**
	 * 提示信息
	 */
	@Transient
	private String falseMessageStr;
	
	/**
	 * 角色所属
	 */
	@Transient
	private String roleOwner;
	

	/**
	 * 提醒人 
	 * workflowdetailsendMessage
	 */
	@Transient
	private String remaindRoles;
 

	public String getRoleOwner() {
		return roleOwner;
	}

	public void setRoleOwner(String roleOwner) {
		this.roleOwner = roleOwner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getNextIsSysAction() {
		return nextIsSysAction;
	}

	public void setNextIsSysAction(Boolean nextIsSysAction) {
		this.nextIsSysAction = nextIsSysAction;
	}

	public Long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}


	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


 

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReasonTitle() {
		return reasonTitle;
	}

	public void setReasonTitle(String reasonTitle) {
		this.reasonTitle = reasonTitle;
	}


	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(String nextStatus) {
		this.nextStatus = nextStatus;
	}

	public Boolean getReasonMandatory() {
		if(CommonUtil.isNull(reasonMandatory))
		{
			return false;
		}
		return reasonMandatory;
	}

	public void setReasonMandatory(Boolean reasonMandatory) {
		this.reasonMandatory = reasonMandatory;
	}



	public String getCurrentStatusName() {
		return currentStatusName;
	}

	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getNextStatusName() {
		return nextStatusName;
	}

	public void setNextStatusName(String nextStatusName) {
		this.nextStatusName = nextStatusName;
	}

	public String getCurrentGraphNode() {
		return currentGraphNode;
	}

	public void setCurrentGraphNode(String currentGraphNode) {
		this.currentGraphNode = currentGraphNode;
	}

	public String getNextGraphNode() {
		return nextGraphNode;
	}

	public void setNextGraphNode(String nextGraphNode) {
		this.nextGraphNode = nextGraphNode;
	}


	public String getActionPreMsg() {
		return actionPreMsg;
	}


	public void setActionPreMsg(String actionPreMsg) {
		if(CommonUtil.isNull(this.actionPreMsg)){
			this.actionPreMsg = "";
		}
		this.actionPreMsg = actionPreMsg;
	}

	public String getRelationFunction() {
		return relationFunction;
	}

	public void setRelationFunction(String relationFunction) {
		this.relationFunction = relationFunction;
	}

	public String getExecuteFunction() {
		return executeFunction;
	}

	public void setExecuteFunction(String executeFunction) {
		this.executeFunction = executeFunction;
	}

	public String getFalseMessage() {
		return falseMessage;
	}

	public void setFalseMessage(String falseMessage) {
		this.falseMessage = falseMessage;
	}

	public String getRelationFunctionName() {
		return relationFunctionName;
	}

	public void setRelationFunctionName(String relationFunctionName) {
		this.relationFunctionName = relationFunctionName;
	}

	public String getFalseMessageStr() {
		return falseMessageStr;
	}

	public void setFalseMessageStr(String falseMessageStr) {
		this.falseMessageStr = falseMessageStr;
	}
	
	
	//--
	public String getStatusDisplay() {
		return "";
	}
	public String getExecuteMethodCode() {
		return "";
	}
	public String getNextStatusAndApUserCode() {
		return "";
	}
	public String getCurrentStatusAndApUserCode() {
		return "";
	}
	public String getAddedRelationCode() {
		return "";
	}

	public String getExecuteFunctionName() {
		return executeFunctionName;
	}
 
	public void setExecuteFunctionName(String executeFunctionName) {
		this.executeFunctionName = executeFunctionName;
	}

	public String getRemaindRoles() {
		return remaindRoles;
	}

	public void setRemaindRoles(String remaindRoles) {
		this.remaindRoles = remaindRoles;
	}

	public Long getWorkFlowSkipPageDefineId() {
		return workFlowSkipPageDefineId;
	}

	public void setWorkFlowSkipPageDefineId(Long workFlowSkipPageDefineId) {
		this.workFlowSkipPageDefineId = workFlowSkipPageDefineId;
	}

	public Boolean getNotExeCloseEntity() {
		return notExeCloseEntity;
	}

	public void setNotExeCloseEntity(Boolean notExeCloseEntity) {
		this.notExeCloseEntity = notExeCloseEntity;
	} 
	
	
  
}
