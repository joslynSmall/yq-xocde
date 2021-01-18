package com.yq.xcode.common.bean;
import java.io.Serializable;

public class ExceptionView implements Serializable {
	private String title;
	private String detail;
	private String message;
	
	public ExceptionView(){
		
	}
    public ExceptionView(String title,String detail){
		this.title = title;
		this.detail = detail;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
