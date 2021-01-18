package com.yq.xcode.security.bean;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.entity.SecPrincipal;
public class PrincipalBean extends XBaseView {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4250701768644015943L;

	private SecPrincipal principal;
	
	private List<Role> roles;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private List<ResourceInstance> resources;
	
	private Map<String,String> resourceNameMap;
	
	public PrincipalBean() {

	}
	public PrincipalBean(SecPrincipal principal) {
		this.principal = principal;
	}
	
	
	public Map<String, String> getResourceNameMap() {
		return resourceNameMap;
	}
	public void setResourceNameMap(Map<String, String> resourceNameMap) {
		this.resourceNameMap = resourceNameMap;
	}
//	public String getResourceFormat() {
//		String resourcesFormat = " ";
//		for(ResourceInstance resource : resources) {	
//			resourcesFormat = resourcesFormat +"【"+ resource.getName()+"】";
//		}
//		if(CommonUtil.isNull(resourcesFormat)) {
//		  return  resourcesFormat;
//		}
//		else {
//		  return resourcesFormat;
//		}
//	}
	public String getRoleFormat() {
		String rolesFormat = " ";
		for(Role role : roles) {	
			rolesFormat = rolesFormat +"【"+ role.getDescription()+"】";
		}
		if(CommonUtil.isNull(rolesFormat)) {
		  return  rolesFormat;
		}
		else {
		  return rolesFormat;
		}
	}
	public SecPrincipal getPrincipal() {
		return principal;
	}
	public void setPrincipal(SecPrincipal principal) {
		this.principal = principal;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	public List<ResourceInstance> getResources() {
		return resources;
	}
	public void setResources(List<ResourceInstance> resources) {
		this.resources = resources;
	}

}
