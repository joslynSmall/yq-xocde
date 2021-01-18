package com.yq.xcode.security.bean;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.bean.ResourceInstance;

public class ResourceAssignmentBean extends XBaseView{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4053621699742078579L;

	private boolean selected;
	
	private ResourceInstance resourceInstance;
	
	private long principalId;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}


	public ResourceInstance getResourceInstance() {
		return resourceInstance;
	}

	public void setResourceInstance(ResourceInstance resourceInstance) {
		this.resourceInstance = resourceInstance;
	}

	public long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(long principalId) {
		this.principalId = principalId;
	}
}
