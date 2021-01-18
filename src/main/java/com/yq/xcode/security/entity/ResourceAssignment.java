package com.yq.xcode.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "sec_resource_assignment")
public class ResourceAssignment extends JpaBaseModel {

	private static final long serialVersionUID = 6362407005244431172L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
	
	@Column(name = "ACTION_MASK")
	private int actionMask;
	
	@Column(name = "RESOURCE_ID")
	private String resourceId;

	@Column(name = "RESOURCE_DEF_ID")
	private Long resourceDefId;
	
	@Column(name = "SID_ID")
	private Long sidId;
	
	
	@Transient
	private SecurityId securityId;
	
	@Transient
	private ResourceDefination resourceDef;

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

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Long getResourceDefId() {
		return resourceDefId;
	}

	public void setResourceDefId(Long resourceDefId) {
		this.resourceDefId = resourceDefId;
	}

	public Long getSidId() {
		return sidId;
	}

	public void setSidId(Long sidId) {
		this.sidId = sidId;
	}

	public ResourceDefination getResourceDef() {
		return resourceDef;
	}

	public void setResourceDef(ResourceDefination resourceDef) {
		this.resourceDef = resourceDef;
	}

	public SecurityId getSecurityId() {
		return securityId;
	}

	public void setSecurityId(SecurityId securityId) {
		this.securityId = securityId;
	}
	

	 
}
