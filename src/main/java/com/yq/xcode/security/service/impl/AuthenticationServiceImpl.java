package com.yq.xcode.security.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.RedisSecurityUtil;
import com.yq.xcode.security.bean.SimpleAuthentication;
import com.yq.xcode.security.entity.SecPrincipal;
import com.yq.xcode.security.oauth.LoginUser;
import com.yq.xcode.security.oauth.PermissionFormatter;
import com.yq.xcode.security.oauth.UserAccessTicket;
import com.yq.xcode.security.oauth.UserAccessToken;
import com.yq.xcode.security.oauth.UserAccessTokenService;
import com.yq.xcode.security.oauth.XUserDetails;
import com.yq.xcode.security.oauth.yq.UserContext;
import com.yq.xcode.security.oauth.yq.YqTokenConstant;
import com.yq.xcode.security.service.AuthenticationService;
import com.yq.xcode.security.service.PrincipalService;


@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	private PrincipalService principalService;
	@Autowired
	private AuthenticationManager authenticationManager;
    @Autowired
    private RedisSecurityUtil redisUtil;
	@Autowired
	private UserAccessTokenService userAccessTokenService;

	@Override
	public SecPrincipal register(SecPrincipal principal) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		final String rawPassword = principal.getPassword();
		principal.setPassword(encoder.encode(rawPassword));
		principalService.savePrincipal(principal);
		return principal;
	}

	@Override
	public void checkPassword(String username, String password) {
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
		authenticationManager.authenticate(upToken);
	}

	@Override
	public void logout(HttpServletRequest request) {
		String tokenValue = request.getParameter(YqTokenConstant.TOKEN_PARAMETER_NAME);
		if (CommonUtil.isNull(tokenValue)) {
			tokenValue = request.getHeader(YqTokenConstant.TOKEN_PARAMETER_NAME);
		}
		redisUtil.remove(YqTokenConstant.USER_SECURITY_KEY, tokenValue);
		redisUtil.remove(YqTokenConstant.USER_ACCESS_TOKEN, tokenValue);
		
	}

	@Override
	public SimpleAuthentication putSecurityContext(Authentication authentication,
			UserAccessTicket ticket) throws Exception{
		UserAccessToken token = userAccessTokenService.createToken(ticket);
		SimpleAuthentication simpleAuthentication = 
				PermissionFormatter.format(ticket.getUsername(), authentication);
		simpleAuthentication.setToken(token);
		Object p = authentication.getPrincipal();
		if (p instanceof XUserDetails) {
			XUserDetails user = (XUserDetails) p ;
			simpleAuthentication.setLoginUser(user.getLoginUser());
		}
		UserContext uc = new UserContext();
		uc.setAuthentication(authentication);
		redisUtil.put(YqTokenConstant.USER_SECURITY_KEY,
				token.getToken(), uc);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return simpleAuthentication;
	}

	@Override
	public Authentication userLogin(UserAccessTicket ticket) {
		UsernamePasswordAuthenticationToken upToken =
				new UsernamePasswordAuthenticationToken(ticket.getUsername(), ticket.getPassword());
		final Authentication authentication = authenticationManager.authenticate(upToken);
		return authentication;
	}
	@Override
	public Authentication getAuthentication(LoginUser loginUser) {
		XUserDetails  userDetails = new XUserDetails(loginUser,null);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
    			(userDetails, loginUser.getPassword(),null);
    	authentication.setDetails(userDetails);
		return authentication;
	}

	@Override
	public Authentication jwtUserLogin(String userName, String password) {
		UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userName, password);
		final Authentication authentication = authenticationManager.authenticate(upToken);
		return authentication;
	}
	
}
