package com.yq.xcode.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sec_role_assignment")
public class RoleAssignment extends JpaBaseModel {

	private static final long serialVersionUID = 1776017807660566550L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
    
	@Column(name = "ROLE_ID")
	private Long roleId;
	
	@Column(name = "SID_ID")
	private Long sidId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getSidId() {
		return sidId;
	}

	public void setSidId(Long sidId) {
		this.sidId = sidId;
	}
	
}
