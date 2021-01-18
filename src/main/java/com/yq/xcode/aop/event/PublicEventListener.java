package com.yq.xcode.aop.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

public class PublicEventListener implements ApplicationListener {

 
	
	private static Log LOG = LogFactory.getLog(PublicEventListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		//LOG.info("event-->"+event.getClass());
		if (event instanceof BaseEvent) {
			BaseEvent wfEvent = (BaseEvent) event;
			wfEvent.run();
		} 
	}
 

	
}
