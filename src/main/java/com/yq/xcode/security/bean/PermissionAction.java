package com.yq.xcode.security.bean;

import com.yq.xcode.common.base.XBaseView;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class PermissionAction extends XBaseView{

	private String name;

	private int mask;

	private boolean selected;
}
