package com.yq.xcode.common.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
//import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

public class SpringUtil {
	public static HttpServletRequest getRequest (){
		HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	public static ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(getRequest().getSession().getServletContext());
	}
	public static Map<String, HandlerMapping> getHandlerMappings() {
		return BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), HandlerMapping.class, true, false);
	}
	
//	public static AnnotationMethodHandlerAdapter getAnnotationMethodHandlerAdapter() {
//		Map<String,AnnotationMethodHandlerAdapter>  tmpMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), AnnotationMethodHandlerAdapter.class);
//		for (AnnotationMethodHandlerAdapter ann : tmpMap.values()) {
//			if (ann.getClass().getName().equals(AnnotationMethodHandlerAdapter.class.getName())) {
//				return ann;
//			} 
//		}
//		return null;
//	}
	
	public static <T> T getBean(String name,Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	public static HandlerExecutionChain getHandlerExecutionChain() {
		Map<String, HandlerMapping> matchingBeans = getHandlerMappings();
		HandlerExecutionChain hec = null;
		for (HandlerMapping hm : matchingBeans.values()) {
			try {
				hec = hm.getHandler(getRequest());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (hec != null) {
				return hec;
			}
		}
		return hec;
	}
	
	public static Object currentAction() {
		HandlerExecutionChain hec = getHandlerExecutionChain();
		if (hec != null) {
			return hec.getHandler();
		}
		System.out.println("无法找到 currentAction ！！");
		return null;
	}
	

	

}
