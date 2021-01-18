package com.yq.xcode.security.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.security.bean.PermissionBean;
import com.yq.xcode.security.criteria.PermissionCriteria;
import com.yq.xcode.security.entity.Permission;
import com.yq.xcode.security.entity.PermissionAssignment;

public interface PermissionService {

	public List<Permission> findAll();
	
	public Page<Permission> findPermissionPage(PermissionCriteria criteria);
	
	public Permission getPermission(Long id);
	
	public void savePermission(Permission permission);
	
	public void deletePermission(Long id);
	
	public List<PermissionAssignment> getRolePermissionBySId(Long sid);
	
	public List<PermissionBean> findPermissionBean(Long roleId);
	
	public List<PermissionBean> findAllPermissionBean(Long roleId);
}
