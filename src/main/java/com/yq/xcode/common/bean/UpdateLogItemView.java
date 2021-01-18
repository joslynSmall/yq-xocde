package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.audit.ChangedProperty;
import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;




public class UpdateLogItemView  extends XBaseView{
	private AuditLogItem auditLogItem;



	public AuditLogItem getAuditLogItem() {
		return auditLogItem;
	}
	public void setAuditLogItem(AuditLogItem auditLogItem) {
		this.auditLogItem = auditLogItem;
	}
	public UpdateLogItemView() {
		
	}
	public UpdateLogItemView(AuditLogItem auditLogItem) {
		this.setAuditLogItem(auditLogItem);
	}
	
	public String getActionName() {
		return AuditLogView.getActionNameByAction(this.auditLogItem.getAction());
	}

	public String getChangedProperties() {
		List<ChangedProperty> cpList = this.auditLogItem.getChangedProperties();
		String htmlStr = "";
		if (!CommonUtil.isNull(cpList)) {
			for (ChangedProperty cp : cpList) {
				if (!this.ignorePro(cp.getName())) {
					htmlStr = htmlStr + AuditLogView.changedPropertyToString(cp) ;
				}
			}
		}
		return htmlStr;
	}

	private boolean ignorePro(String property) {
		return AuditLogView.ignoreProperty.contains(","+property+",");
	}

}
