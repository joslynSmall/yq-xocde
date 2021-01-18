package com.yq.xcode.common.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.yq.xcode.security.entity.JpaBaseModel;
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class YqJpaBaseModel extends JpaBaseModel{
 
	@Transient
	private Boolean disabledPage;
 
	public Boolean getDisabledPage() {
		return disabledPage;
	}
	public void setDisabledPage(Boolean disabledPage) {
		this.disabledPage = disabledPage;
	}
	 
}
