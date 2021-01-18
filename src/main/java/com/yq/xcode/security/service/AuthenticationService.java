package com.yq.xcode.security.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.yq.xcode.security.bean.SimpleAuthentication;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.oauth.LoginUser;
import com.yq.xcode.security.oauth.UserAccessTicket;

public interface AuthenticationService {

	SecPrincipal register(SecPrincipal principal);
	
	void checkPassword(String username, String password);
	
	public SimpleAuthentication putSecurityContext(Authentication authentication,
			UserAccessTicket ticket) throws Exception ;
	
	public Authentication  userLogin(UserAccessTicket userAccessTicket);
	
	public Authentication  getAuthentication(LoginUser loginUser);
	
	public void logout(HttpServletRequest request);
	
	public Authentication jwtUserLogin(String userName, String password);
	
}
