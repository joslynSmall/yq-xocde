package com.yq.xcode.aop.event;

import org.springframework.context.ApplicationEvent;

public abstract class BaseEvent extends ApplicationEvent {

	private static final long serialVersionUID = 7213246619064569795L;
 
	public BaseEvent(Object source) {
		super(source);
	}
 	public abstract void run();
 
}
