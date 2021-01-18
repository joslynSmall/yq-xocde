package com.yq.xcode.common.criteria;

import com.yq.xcode.common.audit.jpa.AuditLogQuery;

public class YqAuditLogQueryCriteria  extends AuditLogQuery { 
	private boolean showHidden = false;

	public boolean isShowHidden() {
		return showHidden;
	}

	public void setShowHidden(boolean showHidden) {
		this.showHidden = showHidden;
	}
	

}
