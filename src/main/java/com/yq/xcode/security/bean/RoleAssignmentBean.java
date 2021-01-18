package com.yq.xcode.security.bean;

import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.security.entity.Role;

public class RoleAssignmentBean extends XBaseView{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4053621610742078579L;

	private boolean selected;
	
	private Role role;
	
	private long principalId;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(long principalId) {
		this.principalId = principalId;
	}
	
}
