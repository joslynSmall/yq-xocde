package com.yq.xcode.common.bean;

import java.util.List;

import com.yq.xcode.common.audit.AuditEvent;
import com.yq.xcode.common.audit.ChangedProperty;
import com.yq.xcode.common.base.XBaseView;
import com.yq.xcode.common.utils.CommonUtil;




public class AuditLogView  extends XBaseView{
	public static final String ignoreProperty = ",id,version,createUser,createTime,lastUpdateUser,lastUpdateTime,";
	private AuditLog auditLog;
	private boolean showHidden = false;


	public AuditLog getAuditLog() {
		return auditLog;
	}

	public void setAuditLog(AuditLog auditLog) {
		this.auditLog = auditLog;
	}

	public AuditLogView() {
		
	}
	public AuditLogView(AuditLog auditLog,boolean showHidden) {
		this.showHidden = showHidden;
		this.setAuditLog(auditLog);
	}

	public String getChangedProperties() {
		if (AuditEvent.ACTION_CREATE.equals(this.auditLog.getAction()) && !this.showHidden ) {
			return "";
		}
		List<ChangedProperty> cpList = this.auditLog.getChangedProperties();
		String htmlStr = "";
		if (!CommonUtil.isNull(cpList)) {
			for (ChangedProperty cp : cpList) {
				if (!this.ignorePro(cp.getName())) {
					htmlStr = htmlStr + changedPropertyToString(cp) ;
				}
			}
		}
		return htmlStr;
	}

	private boolean ignorePro(String property) {
		if (this.showHidden) {
			return false;
		}
		return ignoreProperty.contains(","+property+",");
	}
	
	public String getActionName() {
		return getActionNameByAction(this.auditLog.getAction());
	}
	
	public static String changedPropertyToString(ChangedProperty cp) {
		return "<B>"+ cp.getName()+" :</B> "+vToString( cp.getOldValue())+" <B>=></B> "+vToString(cp.getNewValue())+"<SUB> &nbsp; </SUB><br>";
	}
	private static Object vToString(Object o) {
		if (CommonUtil.isNull(o) || "null".equals(o)) {
			return "{空值}";
		}
		return o;
	}
	
	public static String getActionNameByAction(String action) {
		if (AuditEvent.ACTION_CREATE.equals(action)) {
			return "新增";
		}
		if (AuditEvent.ACTION_UPDATE.equals(action)) {
			return "修改";
		}
		if (AuditEvent.ACTION_DELETE.equals(action)) {
			return "删除";
		}
		if (AuditEvent.ACTION_READ.equals(action)) {
			return "查询";
		}
		return action;
	}
	

}
