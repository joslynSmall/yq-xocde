package com.yq.xcode.security.utils;

import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import com.yq.xcode.security.bean.GrantedPermissions;
import com.yq.xcode.security.bean.GrantedResource;
import com.yq.xcode.security.bean.MaskPermission;
import com.yq.xcode.security.oauth.LoginUser;
import com.yq.xcode.security.oauth.XUserDetails;
import com.yq.xcode.security.oauth.yq.YqAuthenticationToken;
import com.yq.xcode.security.resourceproviders.ResourceConstants;

public class YqSecurityUtils {
	
	public static Authentication getYqAuthenticationToken() {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Authentication auth = getAuthentication();
		if(auth instanceof YqAuthenticationToken) {
			return (YqAuthenticationToken)auth;
		}
		return auth;
	}
	public static boolean hasRole(String name) {
		Set<String> roles = getAssignedRole(getYqAuthenticationToken());
		return roles.contains("ROLE_"+name);
	}
	public static boolean hasResource(String name,String id) {
		Set<String> resources = getResource(name);
		return resources.contains(id);
	}
	
	public static Set<String> getResource(String name) {
		return getAssignedResources(getYqAuthenticationToken(), name, 0);
	}

	public static boolean hasPermission(String name, int mask) {
		PermissionCollection permissions = getPermissions(getAuthentication());
		MaskPermission permission = new MaskPermission(name, mask);
		return permissions.implies(permission);
	}

	public static boolean hasPermission(String name) {
		return hasPermission(name, 1);
	}
 
	public static LoginUser getLoginUser() { 
		return getUser().getLoginUser();
	}
 
	public static Set<String> getWorkFlowRoleResource() {
		// 测试用, 还没有登录
		if ("anonymousUser".equals(getLoginUserKey())) {
			String hdStr = "WFR-FR01,WFR-FR02,WFR-FR03,WFR-FR04,WFR-FR05,WFR-FR06,WFR-FR07,WFR-FR08,WFR-FR09,WFR-FR10,WFR-FR11,WFR-FR12,WFR-FR13,WFR-FR014,WFR-FR015,WFR-FR016,WFR-FR017,WFR-FR018,WFR-FR019,WFR-FR020,WFR-R021,WFR-R022,WFR-FR023,WFR-FR025";
			String[] sa = hdStr.split(",");
			Set<String> rSet = new HashSet<String>();
			for (String r : sa ) {
				rSet.add(r);
			}
			return rSet;
		}
 		return getResource(ResourceConstants.WORK_FLOW_ROLE);
	}

	public static String getLoginUserKey() { 
		return getUser().getUsername();
	}
	public static String getLoginUserDisplay() {
		String displayName = getLoginUser().getDisplayName();
		return getUser().getLoginUser().getDisplayName();
	}
	
	public static PermissionCollection getPermissions(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		PermissionCollection permissions = null;
		for (GrantedAuthority ca : authorities) {
			if (ca instanceof GrantedPermissions) {
				permissions = ((GrantedPermissions) ca).getPermissions();
				break;
			}
		}
		return permissions;
	}
	public static Set<String> getAssignedRole(Authentication authentication) {
		Set<String> idSet = new HashSet<String>();
		for(GrantedAuthority ga : authentication.getAuthorities()) {
			if(ga instanceof SimpleGrantedAuthority) {
				idSet.add(ga.getAuthority());
			}
		}
		return idSet;
	}
	public static Set<String> getAssignedResources(Authentication authentication,String name,int mask) {
		Set<String> idSet = new HashSet<String>();
		for(GrantedAuthority ga : authentication.getAuthorities()) {
			if(ga instanceof GrantedResource) {
				GrantedResource gr = (GrantedResource)ga;
				if(gr.getAuthority().equals(name) && gr.implies(mask)) {
					idSet.addAll(gr.getIdSet());
				}
			}
		}
		return idSet;
	}

	private static AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

	public static XUserDetails getUser() {
		Authentication auth = getAuthentication();
		Object p = auth.getPrincipal();
		if (p instanceof XUserDetails) {
			return (XUserDetails) p;
		} else if ( p instanceof String ) {
			LoginUser lu = new LoginUser();
			lu.setName((String)p);
			lu.setDisplayName((String)p);
			Collection<? extends GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			XUserDetails user = new XUserDetails(lu,authorities);
			return user;
		}
		return null;
	}

 

	public static Authentication getAuthentication() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null ) {
			auth = new YqAuthenticationToken("anonymousUser", "anonymousUser", new ArrayList<GrantedAuthority>()); 
		}
 		return auth;
	}

	private static Object getPrincipal() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static boolean isAuthenticated() {
		Authentication auth = getAuthentication();
		return !trustResolver.isAnonymous(auth);
	}
	
	public static boolean runAs(Authentication auth,boolean overwriteCurrentUser) {
		if(SecurityContextHolder.getContext().getAuthentication() != null && !overwriteCurrentUser) {
			return false;
		} 
  		SecurityContext context = new SecurityContextImpl(auth);
		SecurityContextHolder.setContext(context);
 		SecurityContextHolder.setContext(context);
		return true;
	}
}
