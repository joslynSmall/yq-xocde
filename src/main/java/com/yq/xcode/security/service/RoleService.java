package com.yq.xcode.security.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.yq.xcode.security.criteria.RoleCriteria;
import com.yq.xcode.security.entity.Role;
import com.yq.xcode.security.entity.RoleAssignment;

public interface RoleService {

	public List<Role> findRoleList();
	
	public Page<Role> findRolePage(RoleCriteria criteria);
	
	public Role getRoleById(Long id);
	
	public void saveRole(Role role);
	
	public void saveRolePermission(Role role);
	
	public void deleteRole(Long id);
	
	public List<RoleAssignment> getRoleAssignmentBySId(Long sid);
}
