package com.yq.xcode.common.bean;

import java.io.Serializable;

public class MiniProgram implements Serializable {
	private static final long serialVersionUID = -7945254706501974849L;

	private String appid;
	private String pagePath;

	/**
	 * 是否使用path，否则使用pagepath. 加入此字段是基于微信官方接口变化多端的考虑
	 */
	private boolean usePath = true;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getPagePath() {
		return pagePath;
	}

	public void setPagePath(String pagePath) {
		this.pagePath = pagePath;
	}

	public boolean isUsePath() {
		return usePath;
	}

	public void setUsePath(boolean usePath) {
		this.usePath = usePath;
	}

}