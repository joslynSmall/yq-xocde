package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import com.yq.xcode.security.entity.SecPrincipal;



public interface YqEmailService {

	public void sendExceptionEmail(Exception ex);

	public void sendMail(String templateName, List<SecPrincipal> toUsers,
			List<SecPrincipal> ccUsers, String subject, Map parameters);	
	
}
