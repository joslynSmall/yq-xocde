package com.yq.xcode.aop.event;

import org.springframework.context.ApplicationEvent;

public class InterfaceMessageEvent extends ApplicationEvent {

	private static final long serialVersionUID = -8162147312081629093L;
	
	private Long sourceId;
	private String sourceNumber;
	private String sourceChannel;
	private String jsonStr;
	private String responseJsonStr;
	private String uuid;
	private String oprType;

	public InterfaceMessageEvent(Object source) {
		super(source);
	}

	public InterfaceMessageEvent(Object source, long chainId) {
		super(source);
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceNumber() {
		return sourceNumber;
	}

	public void setSourceNumber(String sourceNumber) {
		this.sourceNumber = sourceNumber;
	}

	public String getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(String sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOprType() {
		return oprType;
	}

	public void setOprType(String oprType) {
		this.oprType = oprType;
	}

	public String getResponseJsonStr() {
		return responseJsonStr;
	}

	public void setResponseJsonStr(String responseJsonStr) {
		this.responseJsonStr = responseJsonStr;
	}
	
}
