package com.yq.xcode.security.service;

import com.yq.xcode.security.entity.SecurityId;

public interface SecurityIdService {

	public SecurityId getSecurityIdById(Long id);
	
	public SecurityId getSecurityIdBySId(Long sid, SecurityId.Type type);
	
	public void saveSecurityId(SecurityId securityId);
	
	public void deleteSecurityId(Long id);
}
