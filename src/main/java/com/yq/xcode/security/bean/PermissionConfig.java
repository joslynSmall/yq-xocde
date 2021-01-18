package com.yq.xcode.security.bean;

import java.security.Permission;

import org.springframework.security.access.SecurityConfig;

public class PermissionConfig extends SecurityConfig{

	private static final long serialVersionUID = 1L;
	
	private Permission permission;
	
	public PermissionConfig(String config) {
		this(new SimplePermission(config));
    }
	
	public PermissionConfig(String config,String[] actions) {
    	this(new SimplePermission(config,actions));
    }
	
	public PermissionConfig(String config,int mask) {
    	this(new MaskPermission(config,mask));
    }
	
	public PermissionConfig(Permission permission) {
    	super(permission.getName());
    	this.permission = permission;
    }

	public Permission getPermission() {
		return permission;
	}

}