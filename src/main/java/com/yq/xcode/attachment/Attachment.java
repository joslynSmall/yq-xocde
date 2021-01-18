package com.yq.xcode.attachment;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yq.xcode.common.utils.CommonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment implements Serializable {

	private static final long serialVersionUID = 4284842275888502628L;

	private Long id;

	private String name;

	private String thumbnailPath;
	
	private String modelName;
	
	private Long modelId;
	
	private String path;
	
	private String orginalPath;
	
	private String type;

	private long size;

	private String label;
	
	private String displayName;
	
	private String imageGroup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	public String getOrginalPath() {
		if(CommonUtil.isNull(orginalPath)) {
			return path;
		}
		return orginalPath;
	}

	public void setOrginalPath(String orginalPath) {
		this.orginalPath = orginalPath;
	}
	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getImageGroup() {
		return imageGroup;
	}

	public void setImageGroup(String imageGroup) {
		this.imageGroup = imageGroup;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Long getModelId() {
		if(modelId == null) {
			modelId= 0L;
		}
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}


}