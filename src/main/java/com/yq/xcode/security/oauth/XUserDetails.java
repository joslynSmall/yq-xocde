package com.yq.xcode.security.oauth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class XUserDetails implements UserDetails {

	private static final long serialVersionUID = 1341315424242193133L;

	private LoginUser loginUser;
	

	private final Collection<? extends GrantedAuthority> authorities;
	
	public XUserDetails() {
		this.authorities =  null;
	}

	public XUserDetails(LoginUser loginUser, Collection<? extends GrantedAuthority> authorities) {
		this.loginUser = loginUser;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

 
	public LoginUser getLoginUser() {
		return loginUser;
	}
  
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return loginUser.getPassword();
	}

	@Override
	public String getUsername() {
		return loginUser.getName();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
