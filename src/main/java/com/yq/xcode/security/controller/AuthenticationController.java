package com.yq.xcode.security.controller;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.bean.Authentication;
import com.yq.xcode.security.bean.GrantedPermissions;
import com.yq.xcode.security.bean.MaskPermission;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.oauth.XUserDetails;
import com.yq.xcode.security.service.AuthenticationService;
import com.yq.xcode.security.utils.YqSecurityUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	UserDetailsService userDetailsService;

//	@GetMapping("/login")
//	public Result<?> createAuthenticationToken(XAuthenticationRequest authenticationRequest)
//			throws AuthenticationException {
//		try {
//			final XAuthenticationResponse response = authenticationService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//			return ResultUtil.ok(response);
//		} catch (BadCredentialsException e) {
//			log.error("", e);
//			return ResultUtil.error("账号密码有误");
//		} catch (Exception e) {
//			log.error("", e);
//			return ResultUtil.error("系统异常");
//		}
//	}
	
	@GetMapping("/info")
	public Result<?> getUserInfo(String token) throws AuthenticationException {
		try {
			Authentication authentication = new Authentication();
			List<String> roles = new ArrayList<String>();
			List<MaskPermission> maskPermissions = new ArrayList<MaskPermission>();
			
			XUserDetails user = YqSecurityUtils.getUser();
			for (GrantedAuthority ca : user.getAuthorities()) {
				if (ca instanceof SimpleGrantedAuthority) {
					roles.add(ca.getAuthority());
				} else if (ca instanceof GrantedPermissions) {
					PermissionCollection permissions = ((GrantedPermissions) ca).getPermissions();
					Enumeration<Permission> elements = permissions.elements();
					while (elements.hasMoreElements()) {
						Permission permission = elements.nextElement();
						if (permission instanceof MaskPermission) {
							MaskPermission maskPermission = (MaskPermission) permission;
							maskPermissions.add(maskPermission);
						}
					}
				}
			}
			authentication.setUsername(user.getUsername());
			authentication.setRoles(roles);
			authentication.setMasks(maskPermissions);
			return ResultUtil.ok(authentication);
		} catch (BadCredentialsException e) {
			return ResultUtil.error("");
		}
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Result<?> register(@RequestBody SecPrincipal principal) {
		return ResultUtil.ok(authenticationService.register(principal));
	}
}
