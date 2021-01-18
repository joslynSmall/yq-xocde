package com.yq.xcode.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import com.yq.xcode.common.bean.QueryDate;
import com.yq.xcode.editor.MyLongEditor;
import com.yq.xcode.editor.MyQueryDateEditor;

 
@RestController
public class BaseController { 
	@InitBinder
	public void initBinder(WebDataBinder binder) {
 		binder.registerCustomEditor(Date.class,new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));  
 		binder.registerCustomEditor(Long.class,new MyLongEditor()); 
 		binder.registerCustomEditor(QueryDate.class,new MyQueryDateEditor(new SimpleDateFormat("yyyy-MM-dd")));
	} 
}
