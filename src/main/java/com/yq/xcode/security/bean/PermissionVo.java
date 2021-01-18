package com.yq.xcode.security.bean;

import lombok.Data;

@Data
public class PermissionVo {

	private String code;
	
	private String name;
	
	private int mask;
	
	private boolean select;
	
	private String actions;
}
