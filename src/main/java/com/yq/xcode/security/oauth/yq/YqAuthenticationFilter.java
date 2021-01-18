package com.yq.xcode.security.oauth.yq;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.RedisSecurityUtil;
import com.yq.xcode.security.oauth.UserAccessTicket;
import com.yq.xcode.security.oauth.UserAccessTokenService;

public class YqAuthenticationFilter extends BasicAuthenticationFilter {
	@Autowired
	private UserAccessTokenService userAccessTokenService;
	@Autowired
	private RedisSecurityUtil redisUtil;


	public YqAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	private String getTokenValue(HttpServletRequest request) {
		final String tokenParameterName = YqTokenConstant.TOKEN_PARAMETER_NAME;
		String tokenValue = request.getParameter(tokenParameterName);
		if (CommonUtil.isNull(tokenValue)) {
			tokenValue = request.getHeader(tokenParameterName);
		}
		return tokenValue;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		final HttpServletResponse response = (HttpServletResponse) servletResponse;
		if (requiresAuthentication(request)) {
			final String userToken = getTokenValue(request);
			if (!CommonUtil.isNull(userToken)) {
				UserAccessTicket ticket = userAccessTokenService.decodeTicket(userToken);
				if (null != ticket && ticket.getExpireTimestamp() > System.currentTimeMillis()) {
					UserContext userContext = redisUtil.get(YqTokenConstant.USER_SECURITY_KEY, userToken);
					successfulAuthentication(request, response, userContext.getAuthentication());
				}
			}
		}
		filterChain.doFilter(request, response);

	}

	private boolean requiresAuthentication(HttpServletRequest request) {
		if (request.getRequestURI().contains("/login")) {
			return false;
		}
		return true;
	}

	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			Authentication authResult) {
		if (logger.isDebugEnabled()) {
			logger.debug("Authentication success: " + authResult);
		}
		SecurityContextHolder.getContext().setAuthentication(authResult);

	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response) {
		SecurityContextHolder.clearContext();
		if (logger.isDebugEnabled()) {
			logger.debug("Cleared security context due to exception");
		}
	}

}
