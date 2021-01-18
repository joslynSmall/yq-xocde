package com.yq.xcode.common.audit.jpa.service.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yq.xcode.common.audit.jpa.AuditLogPo;
import com.yq.xcode.common.audit.jpa.AuditLogQuery;
import com.yq.xcode.common.audit.jpa.service.AuditLogService;


public class AuditLogServiceImpl implements AuditLogService {

	@PersistenceContext private EntityManager em;
	
	@Override
	public AuditLogPo addAuditLog(AuditLogPo auditLog) {
		em.persist(auditLog);
		return auditLog;
	}

	@Override
	public void addAuditLogs(List<AuditLogPo> auditLogs) {
		for(AuditLogPo alog : auditLogs) {
			em.persist(alog);
		}
	}

	@Override
	public AuditLogPo getAuditLog(long id) {
		return em.find(AuditLogPo.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditLogPo> findAuditLogsOfEntity(String entityName,
			String entityId) {
		return em.createQuery("from AuditLogPo where entityName=?1 and entityId=?2")
				.setParameter(1, entityName)
				.setParameter(2, entityId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AuditLogPo> findAuditLogs(AuditLogQuery query) {
		//return createJpaCriteria(query).createQuery(null, em).getResultList();
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<AuditLogPo> findAuditLogs(Pageable pageable, AuditLogQuery query) {
		//return JpaQueryUtils.query(createJpaCriteria(query).createQuery(null, em), pageable);
		return null;
	}

	@Override
	public void clearAllAuditLogs() {
		em.createNativeQuery("delete from FW_AUDIT_LOG").executeUpdate();
	}

	@Override
	public void clearAuditLogsBefore(Date date) {
		em.createNativeQuery("delete from FW_AUDIT_LOG where ACTION_TIME<?1").setParameter(1, date).executeUpdate();
	}
	
	@Override
	public void clearAuditLogsOfEntity(String entityName) {
		em.createNativeQuery("delete from FW_AUDIT_LOG where ENTITY_NAME=?1").setParameter(1, entityName).executeUpdate();
	}

	@Override
	public void clearAuditLogsOfEntity(String entityName, String entityId) {
		em.createNativeQuery("delete from FW_AUDIT_LOG where ENTITY_NAME=? and ENTITY_ID=?")
		.setParameter(1, entityName)
		.setParameter(2, entityId).executeUpdate();
	}
	
//	private JpaCriteria createJpaCriteria(AuditLogQuery query) {
//		JpaCriteria c = new JpaCriteria("AuditLogPo");
//		c.add(CriterionUtils.equals("entityName", query.getEntityName(),true));
//		c.add(CriterionUtils.equals("entityId", query.getEntityId(),true));
//		c.add(CriterionUtils.equals("username", query.getUsername(),true));
//		c.add(CriterionUtils.equals("action", query.getAction(),true));
//		c.add(CriterionUtils.between("actionTime", query.getFromTime(),query.getToTime()));
//		return c;
//	}

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

 

}
