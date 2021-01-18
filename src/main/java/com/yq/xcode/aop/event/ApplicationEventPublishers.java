package com.yq.xcode.aop.event;

import org.springframework.context.ApplicationEventPublisher;

import com.yq.xcode.aop.MethodInvocationContextHolder;

public class ApplicationEventPublishers {

	public static ApplicationEventPublisher immediately() {
		return MethodInvocationContextHolder.getContext().getImmediateEventPublisher();
	}
	
	public static ApplicationEventPublisher afterTransaction() {
		return MethodInvocationContextHolder.getContext().getAfterTransactionEventPublisher();
	}
}
