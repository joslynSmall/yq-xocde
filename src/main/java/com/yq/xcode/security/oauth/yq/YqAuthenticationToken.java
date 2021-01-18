package com.yq.xcode.security.oauth.yq;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class YqAuthenticationToken extends UsernamePasswordAuthenticationToken {
	
	private static final long serialVersionUID = -733207187441607577L;
	public YqAuthenticationToken() {
		super(null, null);
	}
	public YqAuthenticationToken(Object principal, Object credentials) {
		super(principal,credentials);
	}
	
	 public YqAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		 super(principal,credentials,authorities);
	 }
}
