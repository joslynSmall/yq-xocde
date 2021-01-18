package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class JColumn extends XBaseView  {
	private static final long serialVersionUID = -5288719823113782238L;
	private String selectColName;
	private String insertColName;
	private boolean key = false;
	
	public JColumn(String selectColName,String insertColName, boolean key) {
		this.selectColName = selectColName;
		this.insertColName = insertColName;
		this.key = key;
	}

	public String getSelectColName() {
		return selectColName;
	}

	public void setSelectColName(String selectColName) {
		this.selectColName = selectColName;
	}

	public String getInsertColName() {
		return insertColName;
	}

	public void setInsertColName(String insertColName) {
		this.insertColName = insertColName;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		this.key = key;
	}
	
 
	

}
