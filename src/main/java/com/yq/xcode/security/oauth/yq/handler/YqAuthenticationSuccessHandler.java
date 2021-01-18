package com.yq.xcode.security.oauth.yq.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;

@Component
public class YqAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Authentication authentication) throws IOException, ServletException {
		httpServletResponse.setContentType("application/json;charset=utf-8");
		PrintWriter out = httpServletResponse.getWriter();
		Result<String> result = ResultUtil.ok("登录成功！");
		out.write(JSONObject.toJSONString(result));
		out.flush();
		out.close();
	}
}
