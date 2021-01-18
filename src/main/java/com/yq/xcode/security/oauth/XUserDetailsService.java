package com.yq.xcode.security.oauth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface XUserDetailsService extends UserDetailsService {

	List<GrantedAuthority> findGrantedAuthorities(Long principalId);
}
