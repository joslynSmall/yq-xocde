package com.yq.xcode.aop;

public class MethodInvocationContextHolder {

	private static ThreadLocal<MethodInvocationContext> contexts = new ThreadLocal<MethodInvocationContext>();

	public static MethodInvocationContext getContext() {
		return contexts.get();
	}

	public static boolean pushContext() {
		if (contexts.get() == null) {
			contexts.set(new MethodInvocationContext());
			return true;
		}
		return false;
	}

	public static void popContext() {
		contexts.remove();
	}

}
