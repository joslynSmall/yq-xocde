package com.yq.xcode.security.support;

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.yq.xcode.security.bean.MaskPermission;
import com.yq.xcode.security.bean.PermissionConfig;

@Component
public class XPermissionEvaluator implements PermissionEvaluator {

	@Autowired private AccessDecisionManager accessDecisionManager;

	@Override
	public boolean hasPermission(Authentication authentication, Object permissionName, Object mask) {
		return hasPermission(authentication, new MaskPermission(permissionName.toString(), (Integer) mask));
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean hasPermission(Authentication authentication,Object permission) {
		if(permission == null) {
			return false;
		}
		Permission p = null;
		if(permission instanceof Permission) {
			p = (Permission)permission;
		}else {
			String text = permission.toString();
			int delimIndex = text.indexOf(',');
			if(delimIndex > 0) {
				p = new MaskPermission(text.substring(0,delimIndex),Integer.parseInt(text.substring(delimIndex+1)));
			}else {
				p = new MaskPermission(text);
			}
		}
		List<ConfigAttribute> configs = new ArrayList<ConfigAttribute>();
		configs.add(new PermissionConfig(p));
		try{
			accessDecisionManager.decide(authentication, null, configs);
			return true;
		}catch(AccessDeniedException e) {
			
		}catch(InsufficientAuthenticationException e) {
			
		}
		return false;
	}

}
