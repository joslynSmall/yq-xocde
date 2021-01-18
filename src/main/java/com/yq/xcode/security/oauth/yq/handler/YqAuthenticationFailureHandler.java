package com.yq.xcode.security.oauth.yq.handler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;

@Component
public class YqAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = httpServletResponse.getWriter();
        Result<String> result = new Result<String>();
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            result = ResultUtil.error("用户名或密码输入错误，登录失败!");
        } else if (e instanceof DisabledException) {
            result = ResultUtil.error("账户被禁用，登录失败，请联系管理员!");
        } else {
            result = ResultUtil.error("登录失败");
        }
        out.write(JSONObject.toJSONString(result));
        out.flush();
        out.close();
    }
}
