package com.yq.xcode.security.bean;

import java.util.List;

import com.yq.xcode.security.oauth.LoginUser;
import com.yq.xcode.security.oauth.UserAccessToken;

import lombok.Data;

@Data
public class SimpleAuthentication  {
	
	private LoginUser loginUser;
	
	private UserAccessToken token;
	
	private List<String> roles;

	private List<MaskPermission> masks;
	
	public SimpleAuthentication() {
		super();
	}
	
	
}
