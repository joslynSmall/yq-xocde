package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.web.ui.annotation.ColumnLable;
import com.yq.xcode.web.ui.annotation.EditCol;
import com.yq.xcode.web.ui.annotation.EntityDesc;
import com.yq.xcode.web.ui.annotation.GridCol;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_DETAIL_SEND_MSG")

@EntityDesc(name = "", autoGenPage = true, editCols = 3, genService = true, genAction = true, genCriteria = true)
public class WorkFlowDetailSendMsg extends YqJpaBaseModel {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "yq_work_flow_DETAIL_SEND_MSG_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * work_flow_detail_id int comment '流程路径ID'
	 */
	@ColumnLable(name = "流程路径ID")
	@Column(name = "work_flow_detail_id")
	private Long workFlowDetailId;
	/**
	 * TARGET_CATEGORY VARCHAR(40) comment '目标类型（R - ROLE P - PERSON）'
	 * 可以选择人，也可以选择角色， 空代表角色
	 */
	@ColumnLable(name = "目标类型（R - ROLE  P - PERSON）")
	@Column(name = "TARGET_CATEGORY")
	@GridCol(lineNum = 1)
	private String targetCategory;
	/**
	 * TARGET_CODE VARCHAR(40) comment '目标代码' 角色 - 是角色代码 人 - 人带key
	 */
	@ColumnLable(name = "目标代码")
	@Column(name = "TARGET_CODE")
	@GridCol(lineNum = 2)
	@EditCol(lineNum = 2)
	private String targetCode;
	/**
	 * MSG_CODE VARCHAR(40) comment '消息代码' 数据字典
	 */
	@ColumnLable(name = "消息代码")
	@Column(name = "MSG_CODE")
	private String msgCode;
	@Transient
	private String msgCodeDsp;
	/**
	 * DESCRIPTION VARCHAR(200) comment '描述'
	 */
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	@GridCol(lineNum = 3)
	@EditCol(lineNum = 3)
	private String description;
	
	/**
	 * 目标名称， 
	 * 角色名称或用户名称
	 */
	@Transient
	private String targetName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWorkFlowDetailId() {
		return workFlowDetailId;
	}

	public void setWorkFlowDetailId(Long workFlowDetailId) {
		this.workFlowDetailId = workFlowDetailId;
	}

	public String getTargetCategory() {
		return targetCategory;
	}

	public void setTargetCategory(String targetCategory) {
		this.targetCategory = targetCategory;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMsgCodeDsp() {
		return msgCodeDsp;
	}

	public void setMsgCodeDsp(String msgCodeDsp) {
		this.msgCodeDsp = msgCodeDsp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
 
}