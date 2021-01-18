package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.attachment.Attachment;

public class AttachmentView {

	private String modelName;
	private Long modelId;
	private List<Attachment> attachmentList;
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}
	public List<Attachment> getEntityAttachmentList() {
		return attachmentList;
	}
	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}
	
	
	
	
}
