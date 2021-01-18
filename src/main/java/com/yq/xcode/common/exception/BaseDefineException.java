package com.yq.xcode.common.exception;

public class BaseDefineException extends RuntimeException {

	private static final long serialVersionUID = -7067506919151673199L;
	public BaseDefineException() {
		super();
	}
	
	public BaseDefineException(String msg) {
		super(msg);
	}
	
	public BaseDefineException(Throwable cause) {
		super(cause);
	}
	
	public BaseDefineException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
