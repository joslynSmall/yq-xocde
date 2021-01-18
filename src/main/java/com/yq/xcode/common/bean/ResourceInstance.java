package com.yq.xcode.common.bean;

import com.yq.xcode.common.base.XBaseView;

public class ResourceInstance  extends XBaseView{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 335828098251856267L;

	private String id;
	
	private String name;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
