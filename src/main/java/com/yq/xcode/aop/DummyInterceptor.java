package com.yq.xcode.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

public class DummyInterceptor  implements MethodInterceptor,Ordered{

	
	
	public DummyInterceptor() {
	}
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		return mi.proceed();
	}
	
	
	
	@Override
	public int getOrder() {
		return 0;
	}
	
	

}
