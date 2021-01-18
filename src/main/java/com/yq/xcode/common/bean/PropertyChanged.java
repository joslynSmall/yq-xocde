package com.yq.xcode.common.bean;

import java.io.Serializable;

public class PropertyChanged implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2196803437120485001L;
	private String propertyName;
	private String property;
	private Object oldValue;
	private Object newValue;
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Object getOldValue() {
		return oldValue;
	}
	public void setOldValue(Object oldValue) {
		this.oldValue = oldValue;
	}
	public Object getNewValue() {
		return newValue;
	}
	public void setNewValue(Object newValue) {
		this.newValue = newValue;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

}
