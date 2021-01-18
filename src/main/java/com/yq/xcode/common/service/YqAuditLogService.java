package com.yq.xcode.common.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.yq.xcode.common.audit.ChangedProperty;
import com.yq.xcode.common.bean.AuditLogView;
import com.yq.xcode.common.bean.UpdateLogItemView;
import com.yq.xcode.common.criteria.YqAuditLogQueryCriteria;

public interface YqAuditLogService {

	public PageImpl<AuditLogView> findAuditLogs(
			Pageable pageable,YqAuditLogQueryCriteria criteria) ;

	public PageImpl<UpdateLogItemView> findAuditItems(String auditLogId);
	
	public PageImpl<ChangedProperty> findAuditChangeProperty(String auditLogId);
	
	public PageImpl<ChangedProperty> findItemChangeProperty(String auditLogId,String itemEntityId);	

}
