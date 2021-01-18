package com.yq.xcode.common.view;

import java.util.List;

import com.yq.xcode.common.bean.IdAndVersion;

public class AbsControllerRequest {

	private List<IdAndVersion> idv;
	/**
	 * 更新时的值
	 */
	private String value;

	public List<IdAndVersion> getIdv() {
		return idv;
	}

	public void setIdv(List<IdAndVersion> idv) {
		this.idv = idv;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
