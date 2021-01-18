package com.yq.xcode.common.audit.jpa.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yq.xcode.common.audit.jpa.AuditLogPo;
import com.yq.xcode.common.audit.jpa.AuditLogQuery;


public interface AuditLogService {

	public AuditLogPo addAuditLog(AuditLogPo auditLog);

	public void addAuditLogs(List<AuditLogPo> auditLogs);
	
	public AuditLogPo getAuditLog(long id) ;
	
	public List<AuditLogPo> findAuditLogsOfEntity(String entityName,String entityId);
	
	public List<AuditLogPo> findAuditLogs(AuditLogQuery query);
	
	public Page<AuditLogPo> findAuditLogs(Pageable pageable, AuditLogQuery query);
	
	public void clearAllAuditLogs() ;
	
	public void clearAuditLogsBefore(Date date);
	
	public void clearAuditLogsOfEntity(String entityName);
	
	public void clearAuditLogsOfEntity(String entityName,String entityId);
}
