package com.yq.xcode.security.oauth.yq.handler;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;

@Component
public class YqAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Result<String> result = ResultUtil.error("权限不足，无法访问！");
        httpServletResponse.getWriter().write(JSONObject.toJSONString(result));
    }
}