package com.yq.xcode.common.springdata;


public class ActionResult {

	public final static String RESULT_SUCCESS ="success";
	public final static String RESULT_FAILED ="failed";
	
	public final static ActionResult SUCCESS = new ActionResult(ActionResult.RESULT_SUCCESS);
	
	private String result;
	
	private String message;
	
	public ActionResult(){}
	
	public ActionResult(String result) {
		this.result = result;
	}
	
	public ActionResult(String result,String message) {
		this.result = result;
		this.message = message;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}