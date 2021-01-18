package com.yq.xcode.common.exception;

public class ParseExprException extends BaseDefineException {

	private static final long serialVersionUID = -7067506919151673199L;
	public ParseExprException() {
		super();
	}
	
	public ParseExprException(String msg) {
		super(msg);
	}
	
	public ParseExprException(Throwable cause) {
		super(cause);
	}
	
	public ParseExprException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
