package com.yq.xcode.common.audit;

import java.io.Serializable;

public class ChangedProperty implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7007423710640227384L;

	private String name;

	private Object oldValue;

	private Object newValue;
	
	public ChangedProperty() {}
	
	public ChangedProperty(String name,Object oldValue,Object newValue) {
		this.name = name;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer("ChangedProperty[");
		buffer.append("name=").append(name)
		.append(", oldValue=").append(oldValue)
		.append(", newValue=").append(newValue)
		.append("]");
		return buffer.toString();
	}
	
}
