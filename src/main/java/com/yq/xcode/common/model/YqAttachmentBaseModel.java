package com.yq.xcode.common.model;

import com.yq.xcode.attachment.Attachment;

@SuppressWarnings("serial")
public  abstract class YqAttachmentBaseModel extends YqJpaBaseModel {

	/**
	 * Attachment
	 */
	public abstract Attachment obtainAttachmentModel();
	/**
	 * 设置附件url
	 */
	public abstract void allocationAttachment(Attachment attachment);
}

	

