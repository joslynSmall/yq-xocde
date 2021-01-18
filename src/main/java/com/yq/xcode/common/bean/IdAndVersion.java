package com.yq.xcode.common.bean;

import java.io.Serializable;

public class IdAndVersion implements Serializable {
	private static final long serialVersionUID = 2301319358932067778L;
	private Long id;
	private Integer version;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
}
