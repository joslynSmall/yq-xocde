package com.yq.xcode.common.bean;
import java.io.Serializable;

public class FieldTitle implements Serializable {
	private String title;
	private String key; 
	
	public FieldTitle(){
		
	}
    public FieldTitle(String title,String key){
		this.title = title;
		this.key = key; 
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
 

}
