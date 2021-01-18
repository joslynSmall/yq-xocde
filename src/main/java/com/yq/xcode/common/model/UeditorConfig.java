package com.yq.xcode.common.model;

import lombok.Data;

@Data
public class UeditorConfig {

	private String imageUrl;

	private String imagePath;

	private String imageFieldName;

	private int imageMaxSize;

	private String[] imageAllowFiles;

	private String imageActionName;

	private String imageUrlPrefix;
}
