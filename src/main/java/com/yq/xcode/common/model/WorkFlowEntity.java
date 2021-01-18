package com.yq.xcode.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.context.SecurityContextHolder;

import com.yq.xcode.common.bean.RedisIndex;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_entity")

public class WorkFlowEntity extends YqJpaBaseModel {

	@Id
	@Column(name = "ID")
	private Long id;
	/**
	 * ENTITY_ID int not null comment '业务数据ID'
	 * 
	 */
	@ColumnLable(name = "业务数据ID")
	@Column(name = "ENTITY_ID")
	private Long entityId;
	/**
	 * ENTITY_CATEGORY VARCHAR(20) not null comment '业务数量类型，hardcode '
	 * 
	 */
	@RedisIndex
	@ColumnLable(name = "业务数量类型，hardcode ")
	@Column(name = "ENTITY_CATEGORY")
	private String entityCategory;
	/**
	 * ENTITY_NUMBER VARCHAR(20) not null comment '业务数量单号'
	 * 
	 */
	@ColumnLable(name = "业务数量单号")
	@Column(name = "ENTITY_NUMBER")
	private String entityNumber;
	/**
	 * ENTITY_DESCRIPTION VARCHAR(2000) comment '业务数量描述'
	 * 
	 */
	@ColumnLable(name = "业务数量描述")
	@Column(name = "ENTITY_DESCRIPTION")
	private String entityDescription;

	@Column(name = "Work_flow_ID")
	private Long workFlowId;
	
	/**
	 * 旧工作流ID， 流程切换时会将切换前的工作量保存在这里
	 * 
	 */
	@Column(name = "OLD_Work_flow_ID")
	private Long oldWorkFlowId;
	
	/**
	 * 提交日期
	 * 
	 */
	@Column(name = "SUBMIT_DATE")
	private Date submitDate;
	
	/**
	 * 提交员工代码
	 * 
	 */
	@Column(name = "SUBMIT_USER")
	private String submitUser;
	
	/**
	 * 提交人姓名
	 * 
	 */
	@Column(name = "SUBMIT_DISPLAY_NAME")
	private String submitDisplayName;
	
	
	
	/**
	 * ENTITY_STATUS VARCHAR(20) not null comment '业务数量状态'
	 * 
	 */
	@ColumnLable(name = "业务数量状态")
	@Column(name = "ENTITY_STATUS")
	private String entityStatus;
	/**
	 * Graph_NODE VARCHAR(20) comment '图节点'
	 * 
	 */
	@ColumnLable(name = "图节点")
	@Column(name = "Graph_NODE")
	private String graphNode;
	/**
	 * READ_ONLY int
	 * 
	 */
	@Column(name = "READ_ONLY")
	private Boolean readOnly;
	/**
	 * SPEC_ROLE_USER VARCHAR(400) comment '特殊角色'
	 * 特殊角色人，格式为[role1:userCode1][role2:userCode2]
	 */
	@ColumnLable(name = "特殊角色")
	@Column(name = "SPEC_ROLE_USER")
	private String specRoleUser;
	/**
	 * ACTION_REASON VARCHAR(800) comment '当前操作的原因'
	 * 
	 */
	@ColumnLable(name = "原因类型")
	@Column(name = "REASON_TYPE")
	private String reasonType;	
	/**
	 * ACTION_REASON VARCHAR(800) comment '当前操作的原因'
	 */
	@ColumnLable(name = "当前操作的原因")
	@Column(name = "ACTION_REASON")
	private String actionReason;
	/**
	 * LAST_ACTION_CODE VARCHAR(20) comment '前一个操作'
	 * 
	 */
	@ColumnLable(name = "前一个操作")
	@Column(name = "LAST_ACTION_CODE")
	private String lastActionCode;
	
	/**
	 * 已审批完成，已此为依据，转移到历史表
	 * 
	 */
	@ColumnLable(name = "审批完成")
	@Column(name = "IS_CLOSED")
	private Boolean isClosed = false;
	
	
	/**
	 * 如果是系统操作， 将隐藏自动生成的按钮 
	 */
	@ColumnLable(name = "系统操作")
	@Column(name = "IS_SYS_ACTION")
	private Boolean isSysAction = false;

	@ColumnLable(name = "扩展字段1")
	@Column(name = "ATTRIBUTE1")
	private String attribute1;
	@ColumnLable(name = "扩展字段2")
	@Column(name = "ATTRIBUTE2")
	private String attribute2;
	@ColumnLable(name = "扩展字段3")
	@Column(name = "ATTRIBUTE3")
	private String attribute3;
	@ColumnLable(name = "扩展字段4")
	@Column(name = "ATTRIBUTE4")
	private String attribute4;
	@ColumnLable(name = "扩展字段5")
	@Column(name = "ATTRIBUTE5")
	private String attribute5;
	
	@Transient
	public String wxUrl;
	
	@Transient
	private String categoryName;
	
 
	public String getWxUrl() {
		return wxUrl;
	}
	public void setWxUrl(String wxUrl) {
		this.wxUrl = wxUrl;
	}
	

	public Boolean getIsSysAction() {
		return isSysAction;
	}
	public void setIsSysAction(Boolean isSysAction) {
		this.isSysAction = isSysAction;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getEntityNumber() {
		return entityNumber;
	}
	public void setEntityNumber(String entityNumber) {
		this.entityNumber = entityNumber;
	}
	public String getEntityDescription() {
		return entityDescription;
	}
	public void setEntityDescription(String entityDescription) {
		this.entityDescription = entityDescription;
	}
	public Long getWorkFlowId() {
		return workFlowId;
	}
	public void setWorkFlowId(Long workFlowId) {
		this.workFlowId = workFlowId;
	}
	public String getEntityStatus() {
		return entityStatus;
	}
	public void setEntityStatus(String entityStatus) {
		this.entityStatus = entityStatus;
	}
	public String getGraphNode() {
		return graphNode;
	}
	public void setGraphNode(String graphNode) {
		this.graphNode = graphNode;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String getSpecRoleUser() {
		return specRoleUser;
	}
	public void setSpecRoleUser(String specRoleUser) {
		this.specRoleUser = specRoleUser;
	}
	public String getActionReason() {
		return actionReason;
	}
	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
	public String getLastActionCode() {
		return lastActionCode;
	}
	public void setLastActionCode(String lastActionCode) {
		this.lastActionCode = lastActionCode;
	}
	public String getReasonType() {
		return reasonType;
	}
	public void setReasonType(String reasonType) {
		this.reasonType = reasonType;
	}
	
	public Boolean getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}

	public String getAttribute1() {
		return attribute1;
	}
	public void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}
	public String getAttribute2() {
		return attribute2;
	}
	public void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}
	public String getAttribute3() {
		return attribute3;
	}
	public void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}
	public String getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
	public String getAttribute5() {
		return attribute5;
	}
	public void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}
 
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	
	

	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public String getSubmitUser() {
		return submitUser;
	}
	public void setSubmitUser(String submitUser) {
		this.submitUser = submitUser;
	}
	public String getSubmitDisplayName() {
		return submitDisplayName;
	}
	public void setSubmitDisplayName(String submitDisplayName) {
		this.submitDisplayName = submitDisplayName;
	}
 	
	public Long getOldWorkFlowId() {
		return oldWorkFlowId;
	}
	public void setOldWorkFlowId(Long oldWorkFlowId) {
		this.oldWorkFlowId = oldWorkFlowId;
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
}