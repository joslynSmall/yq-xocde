package com.yq.xcode.common.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.yq.xcode.common.bean.RedisIndex;
import com.yq.xcode.web.ui.annotation.ColumnLable;

/**
 * 特殊角色, 会插入到这个表, 不如部门领导, 接单人 等等
 * @author jettie
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "yq_work_flow_ENTITY_ROLE_USER")

public class WorkFlowEntityRoleUser extends YqJpaBaseModel {

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
	 * 角色代码
	 * 
	 */
	@Column(name = "ROLE")
	private String role;
	
	/**
	 * 角色对应的人
	 * 
	 */
	@Column(name = "ROLE_USER")
	private String roleUser;

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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(String roleUser) {
		this.roleUser = roleUser;
	}
	
	 
}