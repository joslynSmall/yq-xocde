package com.yq.xcode.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;
import com.yq.xcode.security.oauth.UserAccessTicket;
import com.yq.xcode.security.oauth.UserAccessToken;
import com.yq.xcode.security.oauth.UserAccessTokenService;

@RestController
@RequestMapping("/userAccessToken")
public class UserAccessTokenController {
	/**
	 * 服务调用token.
	 */
	@Autowired
	private UserAccessTokenService userAccessTokenService;
	   
    /**
	 * 获取token.
	 * @param ticket 参数
	 * @return UserAccessToken 返回值
	 */
    @GetMapping(value = "/getToken")
	public Result<UserAccessToken>  getToken(final UserAccessTicket ticket) {
    	try {
    	  final UserAccessToken token = userAccessTokenService.createToken(ticket);
		  return ResultUtil.ok(token);
		} catch (BadCredentialsException e) {
			return ResultUtil.error(e.getMessage());
    	} catch (Exception ex) {
    		return ResultUtil.error("系统异常，请联系管理员");
    	}
	}
    
    /**
	 * 删除待客户确认的账单数据.
	 * 
	 * @param ticket 参数
	 * @return UserAccessTokenVO
	 */
	@PostMapping(value = "/refreshToken")
	public Result<UserAccessToken> refreshToken(final UserAccessTicket ticket) {
		try {
			final UserAccessToken token = userAccessTokenService.reNewToken(ticket);
			return ResultUtil.ok(token);
		} catch (BadCredentialsException e) {
			return ResultUtil.error(e.getMessage());
		} catch (Exception ex) {
			return ResultUtil.error("系统异常，请联系管理员");
		}
	}
}
