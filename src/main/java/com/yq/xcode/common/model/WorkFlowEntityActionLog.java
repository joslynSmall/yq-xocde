package com.yq.xcode.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.springframework.security.core.context.SecurityContextHolder;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_entity_action_log")

public class WorkFlowEntityActionLog extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "Work_Flow_ENTITY_Action_Log_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * WORK_FLOW_ENTITY_ID int comment '审批流业务数据ID'
	 * 
	 */
	@ColumnLable(name = "审批流业务数据ID")
	@Column(name = "WORK_FLOW_ENTITY_ID")
	private Long workFlowEntityId;
	/**
	 * ENTITY_ID int comment '业务数据ID'
	 * 
	 */
	@ColumnLable(name = "业务数据ID")
	@Column(name = "ENTITY_ID")
	private Long entityId;
	/**
	 * ENTITY_CATEGORY VARCHAR(40) comment '业务类型'
	 * 
	 */
	@ColumnLable(name = "业务类型")
	@Column(name = "ENTITY_CATEGORY")
	private String entityCategory;
	/**
	 * ACTION_DATE datetime comment '操作日期'
	 * 
	 */
	@ColumnLable(name = "操作日期")
	@Column(name = "ACTION_DATE")
	private Date actionDate;
	/**
	 * REASON_TYPE VARCHAR(40) comment '操作原因类型'
	 * 
	 */
	@ColumnLable(name = "操作原因类型")
	@Column(name = "REASON_TYPE")
	private String reasonType;
	/**
	 * ACTION_REASON VARCHAR(800) comment '操作原因'
	 * 
	 */
	@ColumnLable(name = "操作原因")
	@Column(name = "ACTION_REASON")
	private String actionReason;
	/**
	 * ACTION_BY_CODE VARCHAR(40) comment '操作人'
	 * 
	 */
	@ColumnLable(name = "操作人")
	@Column(name = "ACTION_BY_CODE")
	private String actionByCode;
	/**
	 * ACTION_BY_NAME VARCHAR(80) comment '操作人名称'
	 * 
	 */
	@ColumnLable(name = "操作人名称")
	@Column(name = "ACTION_BY_NAME")
	private String actionByName;
	/**
	 * ROLE VARCHAR(40) comment '操作角色'
	 * 
	 */
	@ColumnLable(name = "操作角色")
	@Column(name = "ROLE")
	private String role;
	/**
	 * PRE_STATUS VARCHAR(40) comment '操作前状态'
	 * 
	 */
	@ColumnLable(name = "操作前状态")
	@Column(name = "PRE_STATUS")
	private String preStatus;
	/**
	 * PRE_Graph_NODE VARCHAR(40) comment '操作前流程图节点'
	 * 
	 */
	@ColumnLable(name = "操作前流程图节点")
	@Column(name = "PRE_Graph_NODE")
	private String preGraphNode;
	/**
	 * ACTION VARCHAR(40) comment '操作'
	 * 
	 */
	@ColumnLable(name = "操作")
	@Column(name = "ACTION")
	private String action;
	
	/**
	 * CURRENT_STATUS VARCHAR(40) comment '当前状态'
	 * 
	 */
	@ColumnLable(name = "当前状态")
	@Column(name = "CURRENT_STATUS")
	private String currentStatus;
	/**
	 * CURRENT_Graph_NODE VARCHAR(40) comment '当前流程图节点'
	 * 
	 */
	@ColumnLable(name = "当前流程图节点")
	@Column(name = "CURRENT_Graph_NODE")
	private String currentGraphNode;
	/**
	 * DESCRIPTION VARCHAR(800) comment '描述'
	 * 
	 */
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	private String description;
	
	@ColumnLable(name = "耗时")
	@Column(name = "TAKE_UP_TIME")
	private Integer takeUpTime;
	
	/**
	 * 切换流程用
	 */
	@ColumnLable(name = "流程ID")
	@Column(name = "WORK_FLOW_ID")
	private Long workFlowId;
	
	@ColumnLable(name = "流程明细")
	@Column(name = "WORK_FLOW_DETAIL_ID")
	private Long workFlowDetailId ;
	
	@ColumnLable(name = "部门")
	@Column(name = "Action_By_Dept")
	private String ActionByDept;
	
	
	@Transient
	private String actionName;
	@Transient
	private String roleName;
	@Transient
	private String preStatusName;
	@Transient
	private String currentStatusName;
	@Transient
	private String actionReasonDetail;
	@Transient
	private String color;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWorkFlowEntityId() {
		return workFlowEntityId;
	}
	public void setWorkFlowEntityId(Long workFlowEntityId) {
		this.workFlowEntityId = workFlowEntityId;
	}
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getEntityCategory() {
		return entityCategory;
	}
	public void setEntityCategory(String entityCategory) {
		this.entityCategory = entityCategory;
	}
	public Date getActionDate() {
		return actionDate;
	}
	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	public String getActionReason() {
		return actionReason;
	}
	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
	public String getActionByCode() {
		return actionByCode;
	}
	public void setActionByCode(String actionByCode) {
		this.actionByCode = actionByCode;
	}
	public String getActionByName() {
		return actionByName;
	}
	public void setActionByName(String actionByName) {
		this.actionByName = actionByName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPreStatus() {
		return preStatus;
	}
	public void setPreStatus(String preStatus) {
		this.preStatus = preStatus;
	}
	public String getPreGraphNode() {
		return preGraphNode;
	}
	public void setPreGraphNode(String preGraphNode) {
		this.preGraphNode = preGraphNode;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCurrentStatus() {
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
	public String getCurrentGraphNode() {
		return currentGraphNode;
	}
	public void setCurrentGraphNode(String currentGraphNode) {
		this.currentGraphNode = currentGraphNode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPreStatusName() {
		return preStatusName;
	}
	public void setPreStatusName(String preStatusName) {
		this.preStatusName = preStatusName;
	}
	public String getCurrentStatusName() {
		return currentStatusName;
	}
	public void setCurrentStatusName(String currentStatusName) {
		this.currentStatusName = currentStatusName;
	}
	public String getActionReasonDetail() {
		return actionReasonDetail;
	}
	public void setActionReasonDetail(String actionReasonDetail) {
		this.actionReasonDetail = actionReasonDetail;
	}
	
	@PrePersist
	public void prePersist() {
		String username = "";
		if (CommonUtil.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			username = "SYS";
		} else {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		Date currentTime = new Date();
		this.setCreateTime(currentTime);
		this.setCreateUser(username);
		this.setLastUpdateTime(currentTime);
		this.setLastUpdateUser(username);
	}

 
	public Long getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}
	public Long getWorkFlowDetailId() {
		return workFlowDetailId;
	}
	public void setWorkFlowDetailId(Long workFlowDetailId) {
		this.workFlowDetailId = workFlowDetailId;
	}
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
	
	public Integer getTakeUpTime() {
		return takeUpTime;
	}
	public void setTakeUpTime(Integer takeUpTime) {
		this.takeUpTime = takeUpTime;
	}
	@PreUpdate
	public void preUpdate() {
		String username = "";
		if (CommonUtil.isNull(SecurityContextHolder.getContext().getAuthentication())) {
			username = "SYS";
		} else {
			username = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		this.setLastUpdateTime(new Date());
		this.setLastUpdateUser(username);
	}
	public String getActionByDept() {
		return ActionByDept;
	}
	public void setActionByDept(String actionByDept) {
		ActionByDept = actionByDept;
	}
	
	

}