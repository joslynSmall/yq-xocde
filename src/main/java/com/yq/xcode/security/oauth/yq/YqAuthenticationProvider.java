package com.yq.xcode.security.oauth.yq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.yq.xcode.security.oauth.XUserDetailsService;

@Component
public class YqAuthenticationProvider extends  YqDaoAuthenticationProvider{
	
	@Autowired private XUserDetailsService userService;
	
	@Override
	public boolean supports(Class<?> authentication) {
		  boolean issupport = authentication.equals(UsernamePasswordAuthenticationToken.class);
		 return issupport;
	}
	
	@Override
	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails userDetails) {
		return createSuccessAuthentication(userService,principal,authentication,userDetails);
	}
	
	static Authentication createSuccessAuthentication(XUserDetailsService userService,
			Object principal,
			Authentication authentication, UserDetails userDetails) {
//		UserDetails ud = userService.loadUserByUsername(userDetails.getUsername());
	    /*
	     * 权限等特别处理
	     * 
	     */
		YqAuthenticationToken authenticated = new YqAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		authenticated.setDetails(authentication.getDetails());
		return authenticated;

	}
	
	@Override
	 protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
	      throws AuthenticationException {
		return super.retrieveUser(username, authentication);
	}	
	
}
