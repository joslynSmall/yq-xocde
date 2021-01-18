package com.yq.xcode.security.service;

import java.util.Map;
import java.util.Set;

import com.yq.xcode.security.oauth.LoginUser;

public interface YqSecurityService {

	/**
	 * 取资源， 如果asUserName为空， 取当前登录用户的资源， 否则指定用户资源
	 * @param asUserName
	 * @param sourceName
	 * @return
	 */
	public Set<String> getResource(String asUserName, String sourceName) ;
	
	public LoginUser getLoginUser(String asUserName);
	
	
 
}
