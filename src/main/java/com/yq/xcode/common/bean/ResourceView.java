package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.base.XBaseView;

public class ResourceView  extends XBaseView{
	 /**
	  *  权限资源名称， 
	  *  例如 WorkFlowRole - 审批角色 
	  *     chain - 客户资源 
	  */
	 private String resourceName;  
	 /**
	  * 对应的资源值
	  */
	 private List<String> resourceValueList;
	 /**
	  * 对应资源实例
	  */
	 private List<ResourceInstance> resourceInstanceList;
	 
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public List<String> getResourceValueList() {
		return resourceValueList;
	}
	public void setResourceValueList(List<String> resourceValueList) {
		this.resourceValueList = resourceValueList;
	}
	public List<ResourceInstance> getResourceInstanceList() {
		return resourceInstanceList;
	}
	public void setResourceInstanceList(List<ResourceInstance> resourceInstanceList) {
		this.resourceInstanceList = resourceInstanceList;
	}

}
