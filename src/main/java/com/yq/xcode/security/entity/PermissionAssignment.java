package com.yq.xcode.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sec_permission_assignment")
public class PermissionAssignment extends JpaBaseModel {

	private static final long serialVersionUID = -6977828644872711112L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
    
	@Column(name = "ACTION_MASK")
	private int actionMask;
	
	@Column(name = "PERMISSION_ID")
	private Long permissionId;
	
	@Column(name = "SID_ID")
	private Long sidId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getActionMask() {
		return actionMask;
	}

	public void setActionMask(int actionMask) {
		this.actionMask = actionMask;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

	public Long getSidId() {
		return sidId;
	}

	public void setSidId(Long sidId) {
		this.sidId = sidId;
	}
	
}