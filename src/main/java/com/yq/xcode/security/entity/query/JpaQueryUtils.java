package com.yq.xcode.security.entity.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JpaQueryUtils {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryResult query(Query query,QueryCriteria queryCriteria) {
		if(queryCriteria == null || !queryCriteria.isPaginate()) {
			List result = query.getResultList();
			if(queryCriteria != null) {
				queryCriteria.setTotalRecords(result.size());
				queryCriteria.setDisplayLength(result.size());
			}
			return new QueryResult(queryCriteria,result);
		}
		if(queryCriteria.getTotalRecords() > 0) {
			
		}else {
			queryCriteria.setTotalRecords(query.getResultList().size());
		}
		int firstResult = queryCriteria.getDisplayStart();
		if(firstResult < 0) {
			firstResult = 0;
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(queryCriteria.getDisplayLength());
		return new QueryResult(queryCriteria,query.getResultList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryResult query(Query countQuery,Query query,QueryCriteria queryCriteria) {
		if(queryCriteria == null || queryCriteria.getDisplayLength() <= 0) {
			if(queryCriteria.getTotalRecords() > 0) {
				query.setMaxResults(queryCriteria.getTotalRecords());
			}
			return new QueryResult(queryCriteria,query.getResultList());
		}
		if(queryCriteria.getTotalRecords() > 0) {
			
		}else {
			if(countQuery != null) {
				int ttlCount = ((Long)countQuery.getSingleResult()).intValue();
				queryCriteria.setTotalRecords(ttlCount);
			}else {
				queryCriteria.setTotalRecords(query.getResultList().size());
			}
		}
		int firstResult = queryCriteria.getDisplayStart();
		if(firstResult < 0) {
			firstResult = 0;
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(queryCriteria.getDisplayLength());
		return new QueryResult(queryCriteria,query.getResultList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static QueryResult query(EntityManager entityManager,JpaCriteria jpaCriteria,QueryCriteria queryCriteria) {
		if(queryCriteria == null  || queryCriteria.getDisplayLength() <= 0) {
			Query query = jpaCriteria.createQuery(null, entityManager);
			List result = query.getResultList();
			if(queryCriteria != null) {
				queryCriteria.setTotalRecords(result.size());
				queryCriteria.setDisplayLength(result.size());
			}
			return new QueryResult(queryCriteria,result);
		}
		if(queryCriteria.getTotalRecords() == 0) {
			int ttlCount = ((Long)jpaCriteria.createQuery("select count(*) ", entityManager,false).getSingleResult()).intValue();
			queryCriteria.setTotalRecords(ttlCount);
		}
		Query query = jpaCriteria.createQuery(null, entityManager);
		int firstResult = queryCriteria.getDisplayStart();
		if(firstResult < 0) {
			firstResult = 0;
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(queryCriteria.getDisplayLength());
		return new QueryResult(queryCriteria,query.getResultList());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Page query(Query query,Pageable pageable) {
		if(pageable == null || pageable.getPageSize() <= 0) {
			List result = query.getResultList();
			return new PageImpl(result,pageable,result.size());
		}
		int ttlRecords = query.getResultList().size();
		int firstResult = pageable.getPageSize() * pageable.getPageNumber();
		if(firstResult < 0) {
			firstResult = 0;
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(pageable.getPageSize());
		return new PageImpl(query.getResultList(),pageable,ttlRecords);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Page query(Query countQuery,Query query,Pageable pageable) {
		if(pageable == null || pageable.getPageSize() <= 0) {
			List result = query.getResultList();
			return new PageImpl(result,pageable,result.size());
		}
		int ttlRecords = 0;
		if(countQuery != null) {
			ttlRecords = ((Long)countQuery.getSingleResult()).intValue();
		}else {
			ttlRecords = query.getResultList().size();
		}
		Long firstResult = pageable.getOffset();
		if(firstResult < 0) {
			firstResult = 0L;
		}
		query.setFirstResult(firstResult.intValue());
		query.setMaxResults(pageable.getPageSize());
		return new PageImpl(query.getResultList(),pageable,ttlRecords);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Page query(EntityManager entityManager,JpaCriteria jpaCriteria,Pageable pageable) {
		if(pageable == null  || pageable.getPageSize() <= 0) {
			Query query = jpaCriteria.createQuery(null, entityManager);
			List result = query.getResultList();
			return new PageImpl(result,pageable,result.size());
		}
		int ttlRecords = ((Long)jpaCriteria.createQuery("select count(*) ", entityManager,false).getSingleResult()).intValue();
		Query query = jpaCriteria.createQuery(null, entityManager);
		Long firstResult = pageable.getOffset();
		if(firstResult < 0) {
			firstResult = 0L;
		}
		query.setFirstResult(firstResult.intValue());
		query.setMaxResults(pageable.getPageSize());
		return new PageImpl(query.getResultList(),pageable,ttlRecords);
	}
}
