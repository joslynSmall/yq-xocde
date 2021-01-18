package com.yq.xcode.attachment;


public class ImageAttachment extends Attachment{

	private static final long serialVersionUID = -1241836526757850296L;

	private int width;
	
	private int height;
	
	private int originalWidth;
	
	private int originalHeight;
	
	private long originalSize;
	
	private int thumbnailWidth;
	
	private int thumbnailHeight;
	
	private long thumbnailSize;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getOriginalWidth() {
		return originalWidth;
	}

	public void setOriginalWidth(int originalWidth) {
		this.originalWidth = originalWidth;
	}

	public int getOriginalHeight() {
		return originalHeight;
	}

	public void setOriginalHeight(int originalHeight) {
		this.originalHeight = originalHeight;
	}

	public long getOriginalSize() {
		return originalSize;
	}

	public void setOriginalSize(long originalSize) {
		this.originalSize = originalSize;
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	public long getThumbnailSize() {
		return thumbnailSize;
	}

	public void setThumbnailSize(long thumbnailSize) {
		this.thumbnailSize = thumbnailSize;
	}
	
	
}
