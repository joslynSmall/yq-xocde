package com.yq.xcode.security.bean;

import java.security.PermissionCollection;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GrantedPermissions implements GrantedAuthority {

	private static final long serialVersionUID = 2644761550361790919L;
	 
	private PermissionCollection permissions;
	
	public GrantedPermissions() {
	}
	public GrantedPermissions(PermissionCollection permissions) {
		this.permissions = permissions;
	}

	@Override
	public String getAuthority() {
		return "_permissions";
	}

	public PermissionCollection getPermissions() {
		return permissions;
	}

	@Override
	public String toString() {
		return new StringBuffer("GrantedPermissions[").append(permissions).append("]").toString();
	}

}