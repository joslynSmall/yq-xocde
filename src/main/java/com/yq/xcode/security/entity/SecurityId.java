package com.yq.xcode.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sec_sid")
public class SecurityId extends JpaBaseModel {
	private static final long serialVersionUID = -698146346797453646L;
	public static enum Type {
		PRINCIPAL,
		ROLE
	}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
    private Long id;
    
	@Column(name = "SID")
	private Long sid;

	@Column(name = "TYPE")
	private Type type;
	
	@Column(name = "REALM_ID")
	private Long realmId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Long getRealmId() {
		return realmId;
	}

	public void setRealmId(Long realmId) {
		this.realmId = realmId;
	}
	
}
