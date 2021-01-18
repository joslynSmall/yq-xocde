package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class ParseStrModel  extends XBaseView {
    
	private String preStr;
	private String currStr;
	private String postStr;
	private boolean splitted = false;
	
	public String getPreStr() {
		return preStr;
	}
	public void setPreStr(String preStr) {
		this.preStr = preStr;
	}
	public String getCurrStr() {
		return currStr;
	}
	public void setCurrStr(String currStr) {
		this.currStr = currStr;
	}
	public String getPostStr() {
		return postStr;
	}
	public void setPostStr(String postStr) {
		this.postStr = postStr;
	}
	public boolean isSplitted() {
		return splitted;
	}
	public void setSplitted(boolean splitted) {
		this.splitted = splitted;
	}

}
