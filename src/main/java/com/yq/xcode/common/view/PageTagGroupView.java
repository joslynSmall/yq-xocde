package com.yq.xcode.common.view;

import java.util.List;

import com.yq.xcode.common.bean.IdAndVersion;

public class PageTagGroupView {

	private List<IdAndVersion> idvs;
	private Integer groupLineNumber;
	private String groupName;
	private String groupRemark;
	public List<IdAndVersion> getIdvs() {
		return idvs;
	}
	public void setIdvs(List<IdAndVersion> idvs) {
		this.idvs = idvs;
	}
	public Integer getGroupLineNumber() {
		return groupLineNumber;
	}
	public void setGroupLineNumber(Integer groupLineNumber) {
		this.groupLineNumber = groupLineNumber;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupRemark() {
		return groupRemark;
	}
	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}
	
	
	
}
