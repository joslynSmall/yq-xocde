package com.yq.xcode.editor;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;

import com.yq.xcode.common.bean.QueryDate;

public class MyQueryDateEditor extends PropertyEditorSupport  {
	private DateFormat dateFormat;
	public MyQueryDateEditor(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.trim().equals("")) {
 			setValue(null);
		} else {
			try {
				setValue(new QueryDate(this.dateFormat.parse(text)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
 
		}		
	}

	@Override
	public String getAsText() {
		QueryDate value = (QueryDate) getValue();
		return (value != null ? this.dateFormat.format(value) : "");
	}
	
}
