package com.yq.xcode.aop.event;

import java.util.LinkedList;
import java.util.List;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;

public class SyncEventPublisher implements ApplicationEventPublisher{

	private List<ApplicationEvent> eventQueue = new LinkedList<ApplicationEvent>();
	@Override
	public void publishEvent(ApplicationEvent event) {
		eventQueue.add(event);
	}
	
	public List<ApplicationEvent> getEventQueue() {
		return eventQueue;
	}

	@Override
	public void publishEvent(Object event) {
		
	}

	

	
	
}
