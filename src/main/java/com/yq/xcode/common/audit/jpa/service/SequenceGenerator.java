package com.yq.xcode.common.audit.jpa.service;

import org.springframework.beans.factory.InitializingBean;

public interface SequenceGenerator  extends InitializingBean {
	
	public long nextSequence(String sequenceId);
	
	public long nextSequence(String sequenceId,int interval);
	
	public String nextTextSequence(String prefix,String suffix,int numberSize,String sequenceId);
}
 