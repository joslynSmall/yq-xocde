package com.yq.xcode.security.bean;

import java.util.List;

import com.yq.xcode.common.base.XBaseView;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PermissionBean extends XBaseView {

	private Long permissionId;
	
	private String code;

	private String name;

	private List<PermissionAction> actions;

}
