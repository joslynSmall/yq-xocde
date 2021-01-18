package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowSpecRole;
import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_ENTITY_TEST")

public class WorkFlowEntityTest extends JpaBaseModel implements WorkFlowEntityIntf {

	@TableGenerator(name = "idGen", table = "ID_GENERATOR", pkColumnName = "ID_KEY", valueColumnName = "ID_VALUE", pkColumnValue = "SO_WORK_FLOW_ENTITY_TEST_ID", allocationSize = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "idGen")
	@Column(name = "ID")
	private Long id;
	/**
	 * TEST_NUMBER VARCHAR(20) not null comment '盘点单号'
	 * 
	 */
	@ColumnLable(name = "盘点单号")
	@Column(name = "TEST_NUMBER")
	private String testNumber;
	/**
	 * STATUS VARCHAR(20) comment '状态'
	 * 
	 */
	@ColumnLable(name = "状态")
	@Column(name = "STATUS")
	private String status;
	/**
	 * DESCRIPTION VARCHAR(100) comment '描述'
	 * 
	 */
	@ColumnLable(name = "描述")
	@Column(name = "DESCRIPTION")
	
	@Transient
	private String actionReason;
	
	private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(String testNumber) {
		this.testNumber = testNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getEntityCategory() {
		// TODO Auto-generated method stub
		return "WF-TEST";
	}
	@Override
	public String getEntityDescription() {
		// TODO Auto-generated method stub
		return this.getDescription();
	}
	@Override
	public Long getEntityId() {
		// TODO Auto-generated method stub
		return this.getId();
	}
	@Override
	public String getEntityNumber() {
		// TODO Auto-generated method stub
		return this.getTestNumber();
	}
	@Override
	public String getEntityStatus() {
		// TODO Auto-generated method stub
		return this.getStatus();
	}
	@Override
	public WorkFlowSpecRole[] getSpecRoles() {
		// TODO Auto-generated method stub
		return null;
	}
 
	public String getActionReason() {
		return actionReason;
	}
	public void setActionReason(String actionReason) {
		this.actionReason = actionReason;
	}
 
	@Override
	public void reSetEntityStatus(String entityStatus) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void reSetWorkFlowId(Long workFlowId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Long getItWorkFlowId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getEntityStatusDsp() { 
		return null;
	}
	@Override
	public String getCreateByDsp() { 
		return null;
	}
 
}