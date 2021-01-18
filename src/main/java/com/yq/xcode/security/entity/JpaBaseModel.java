package com.yq.xcode.security.entity;


import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class JpaBaseModel extends JpaAuditableModel{
	
	private static final long serialVersionUID = 2301319358932067778L;

	public abstract Long getId();
	
	public abstract void setId(Long id);
	
}
