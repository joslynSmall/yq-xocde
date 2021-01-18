package com.yq.xcode.common.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.Result;
import com.yq.xcode.common.utils.ResultUtil;
@RestController
@RequestMapping("/demoTableData")
public class XcodeDemoController { 
	@RequestMapping(value = {""}, method = RequestMethod.POST)
  	public Result findSelectItemListByCategory(HttpServletResponse response){

		return ResultUtil.ok("发送审批成功！");
	}
 
 
}
