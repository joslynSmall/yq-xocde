package com.yq.xcode.attachment;


public class ImageFileRequest {

	private String modelName;
	
	private Long modelId;
	
	private int sequence;
	
	private String imageGroup;
	
	/**
	 * 唯一附件
	 */
	private boolean unique;
	
	/**
	 * 压缩图宽度
	 */
	private int scaleWidth = 100;
	
	/**
	 * 压缩图高度
	 */
	private int scaleHeight = 100;

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

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getImageGroup() {
		return imageGroup;
	}

	public void setImageGroup(String imageGroup) {
		this.imageGroup = imageGroup;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public int getScaleWidth() {
		return scaleWidth;
	}

	public void setScaleWidth(int scaleWidth) {
		this.scaleWidth = scaleWidth;
	}

	public int getScaleHeight() {
		return scaleHeight;
	}

	public void setScaleHeight(int scaleHeight) {
		this.scaleHeight = scaleHeight;
	}
	
}
