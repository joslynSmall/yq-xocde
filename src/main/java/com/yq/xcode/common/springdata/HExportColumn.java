package com.yq.xcode.common.springdata;

import java.io.Serializable;

public class HExportColumn implements Serializable{

	private static final long serialVersionUID = 5112720481011229387L;
	
	private String field;
	private String title;
	private Boolean hidden;
	private String width;
	private int sequence;
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getHidden() {
		return hidden;
	}
	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
}
