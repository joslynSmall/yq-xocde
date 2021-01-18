package com.yq.xcode.common.audit.jpa.service;

public interface YqSequenceGenerator extends SequenceGenerator{
	
	/**
	 * 一次增加多少个sequence， 这样就可以缓存区值
	 * 只有在  
	 * allowPreallocate 设为true 时才有用
	 * @param sequenceId
	 * @param interval
	 */
	public void addSequence(String sequenceId,int interval); 
	 
 	
}
