package com.yq.xcode.security.access;

import java.security.PermissionCollection;
import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import com.yq.xcode.security.bean.PermissionConfig;
import com.yq.xcode.security.utils.YqSecurityUtils;

public class PermissionVoter implements AccessDecisionVoter<Object> {

	@Override
	public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
		return PermissionConfig.class.isAssignableFrom(clazz);
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return attribute instanceof PermissionConfig;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		PermissionCollection permissions = YqSecurityUtils.getPermissions(authentication);
		for (ConfigAttribute ca : attributes) {
			if (this.supports(ca)) {
				PermissionConfig permConfig = (PermissionConfig) ca;
				if (permissions != null && permissions.implies(permConfig.getPermission())) {
					return ACCESS_GRANTED;
				}
				return ACCESS_DENIED;
			}
		}
		return ACCESS_ABSTAIN;
	}

}
