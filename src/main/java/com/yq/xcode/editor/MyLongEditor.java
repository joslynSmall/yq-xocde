package com.yq.xcode.editor;

import java.beans.PropertyEditorSupport;

public class MyLongEditor extends PropertyEditorSupport  {
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.trim().equals("")) {
			setValue(null);
		} else {
			setValue(Long.parseLong(text));
		}		
	}

	@Override
	public String getAsText() {
		Long value = (Long) getValue();
		if (value == null) {
			return "";
		}
		return value.toString();
	}
	
}
