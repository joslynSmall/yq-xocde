package com.yq.xcode.security.oauth.yq;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.yq.xcode.security.bean.GrantedPermissions;

public interface GrantedAuthoritiesService {
	
	public List<GrantedAuthority> findGrantedAuthorities(String username,String realmCode);
	
	public GrantedPermissions findPermissionsOfRole(String roleCode,String realmCode);
}
