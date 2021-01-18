package com.yq.xcode.common.bean;

import java.util.Map;

import com.yq.xcode.common.base.XBaseView;

 
public class ParseRootAndVariable extends XBaseView {
	private Object rootObject;
	private Map variable;
	public ParseRootAndVariable(){}
	public ParseRootAndVariable(Object rootObject, Map variable){
		this.rootObject = rootObject;
		this.variable = variable;
	}
	 
	public Object getRootObject() {
		return rootObject;
	}
	public void setRootObject(Object rootObject) {
		this.rootObject = rootObject;
	}
	public Map getVariable() {
		return variable;
	}
	public void setVariable(Map variable) {
		this.variable = variable;
	}
	
 
}