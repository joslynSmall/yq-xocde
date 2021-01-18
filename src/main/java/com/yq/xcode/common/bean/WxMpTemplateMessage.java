package com.yq.xcode.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpTemplateMessage implements Serializable {

	private static final long serialVersionUID = 173534547550850527L;

	/**
	 * 接收者openid.
	 */
	private String toUser;

	/**
	 * 模板ID.
	 */
	private String templateId;

	/**
	 * 模板跳转链接.
	 * 
	 * <pre>
	 * url和miniprogram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。
	 * 开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
	 * </pre>
	 */
	private String url;

	private MiniProgram miniProgram;

	/**
	 * 模板数据.
	 */
	private List<WxMpTemplateData> data;

	public WxMpTemplateMessage addData(WxMpTemplateData datum) {
		if (this.data == null) {
			this.data = new ArrayList<WxMpTemplateData>();
		}
		this.data.add(datum);
		return this;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<WxMpTemplateData> getData() {
		return data;
	}

	public void setData(List<WxMpTemplateData> data) {
		this.data = data;
	}

	public MiniProgram getMiniProgram() {
		return miniProgram;
	}

	public void setMiniProgram(MiniProgram miniProgram) {
		this.miniProgram = miniProgram;
	}

}
