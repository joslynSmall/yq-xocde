package com.yq.xcode.aop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;

import com.yq.xcode.aop.event.SyncEventPublisher;

public class MethodInvocationContext {

	private ApplicationEventPublisher afterTransactionEventPublisher = new SyncEventPublisher();
	private ApplicationEventPublisher immediateEventPublisher;
	
	private List<FailedCallback> failedCallbacks = new ArrayList<FailedCallback>();
	private List<SuccessCallback> successCallbacks = new ArrayList<SuccessCallback>();
	
	public void failed(FailedCallback callback) {
		failedCallbacks.add(callback);
	}

	public void success(SuccessCallback callback) {
		successCallbacks.add(callback);
	}

	

	public List<FailedCallback> getFailedCallbacks() {
		return failedCallbacks;
	}

	public List<SuccessCallback> getSuccessCallbacks() {
		return successCallbacks;
	}

	public ApplicationEventPublisher getImmediateEventPublisher() {
		return immediateEventPublisher;
	}

	public void setImmediateEventPublisher(ApplicationEventPublisher immediateEventPublisher) {
		this.immediateEventPublisher = immediateEventPublisher;
	}

	public ApplicationEventPublisher getAfterTransactionEventPublisher() {
		return afterTransactionEventPublisher;
	}

	
	
	

}
