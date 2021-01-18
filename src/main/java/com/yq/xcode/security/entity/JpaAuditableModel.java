package com.yq.xcode.security.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class JpaAuditableModel implements Serializable {

	private static final long serialVersionUID = 6928733860292815318L;

	@Version
	@Column(name = "VERSION")
	private long version;
	
	@Column(name = "CREATED_BY", nullable = true, length = 30)
	private String createUser;
	
	@Column(name = "CREATED_TIME", nullable = true)
	private Date createTime;
	
	@Column(name = "LAST_UPDATED_BY", nullable = true, length = 30)
	private String lastUpdateUser;
	
	@Column(name = "LAST_UPDATED_TIME", nullable = true)
	private Date lastUpdateTime;
	
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	@PrePersist
	public void prePersist() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if(auth != null) {
			username = auth.getName();
		}
		Date currentTime = new Date();
		this.setCreateUser(username);
		this.setCreateTime(currentTime);
		this.setLastUpdateUser(username);
		this.setLastUpdateTime(currentTime);
		
	}

	@PreUpdate
	public void preUpdate() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if(auth != null) {
			username = auth.getName();
		}
		this.setLastUpdateUser(username);
		this.setLastUpdateTime(new Date());
	}
	
	
}
