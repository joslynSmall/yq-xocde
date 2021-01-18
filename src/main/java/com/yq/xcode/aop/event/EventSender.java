package com.yq.xcode.aop.event;

import org.springframework.context.ApplicationEvent;

public class EventSender<T extends ApplicationEvent> {
	
	private T event;
	
	public EventSender(T event) {
		super();
		this.event = event;
	}
	
	public void sendEvent() {
		ApplicationEventPublishers.afterTransaction().publishEvent((ApplicationEvent) event);
	}
	
}
