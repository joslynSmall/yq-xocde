package com.yq.xcode.common.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import com.yq.xcode.common.utils.UpdateUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.aop.event.ApplicationEventPublishers;
import com.yq.xcode.common.audit.AuditEvent;
import com.yq.xcode.common.audit.AuditItem;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.InsertSql;
import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.bean.QueryModel;
import com.yq.xcode.common.exception.DllException;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.security.entity.JpaBaseModel;
import com.yq.xcode.web.ui.annotation.Detail;

@Transactional
public class YqJpaDataAccessObject implements InitializingBean {
	// 开始 以下内容从JpaDataAccessObject复制 改为不继承 为了使用两个EntityManager 192行结束
	@PersistenceContext(unitName = "default")
	protected EntityManager em;
	@Autowired
	private YqSequenceService idGenerator;

	public <E> E getById(Class<E> clazz, Serializable id) {
		return em.find(clazz, id);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public <E extends JpaBaseModel> E create(E entity) {
		if (entity.getId() != null && entity.getId() == 0l  ) {
			entity.setId(null);
		}
		if (this.isManualIdEntity(entity.getClass())) {
			if (entity.getId() == null || entity.getId() == 0l) {
				this.idGenerator.genId(entity);
			}
		}
		em.persist(entity);
		return entity;
	}

	public <E extends JpaBaseModel> List<E> create(List<E> entities) {
		if (CommonUtil.isNull(entities)) {
			return entities;
		}
		if (this.isManualIdEntity(entities.get(0).getClass())) {
			this.createManualIdEntities(entities);
		} else {
			for (E entity : entities) {
				em.persist(entity);
			}
		}
		return entities;
	}

	/**
	 * 是否为指定ID
	 * 
	 * @param entityClass
	 * @return
	 */
	private boolean isManualIdEntity(Class<? extends JpaBaseModel> entityClass) {
		GeneratedValue generator = null;
		try {
			generator = entityClass.getDeclaredField("id").getAnnotation(GeneratedValue.class);
			if (generator == null) {  
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public <E extends JpaBaseModel> List<E> createManualIdEntities(List<E> entities) {
		if (CommonUtil.isNull(entities)) {
			return null;
		}
		if (!this.isManualIdEntity(entities.get(0).getClass())) {
			throw new ValidateException("有定义ID生成策略的，不可用createManualIdEntities（） 方法创建记录!");
		}
		if (entities.get(0).getId() == null || entities.get(0).getId() == 0l) { // 程序未指定ID,新增批量指定ID
			Long startId = this.idGenerator.idNext(entities.get(0).getClass(), entities.size());
			for (int i = 0; i < entities.size(); i++) {
				E entity = entities.get(i);
				entity.setId(startId + i);
				this.em.persist(entity);
			}
		} else {// 传入对象已经指定的id， 一般是数据迁移， 原ID不变,会在外边出入ID
			for (E entity : entities) {
				this.em.persist(entity);
			}
		}

		return entities;

	}

	/**
	 * 先genID 解决保存ID的问题
	 * 
	 * @param entities
	 * @return
	 */
	protected <E extends JpaBaseModel> List<E> genManualIdEntities(List<E> entities) {
		if (CommonUtil.isNull(entities)) {
			return null;
		}
		if (!this.isManualIdEntity(entities.get(0).getClass())) {
			throw new ValidateException("有定义ID生成策略的，不可用createManualIdEntities（） 方法创建记录!");
		}
		if (entities.get(0).getId() == null || entities.get(0).getId() == 0l) { // 程序未指定ID,新增批量指定ID
			Long startId = this.idGenerator.idNext(entities.get(0).getClass(), entities.size());
			for (int i = 0; i < entities.size(); i++) {
				E entity = entities.get(i);
				entity.setId(startId + i);
				// this.em.persist(entity);
			}
		} else {// 传入对象已经指定的id， 一般是数据迁移， 原ID不变,会在外边出入ID
			// for (E entity : entities) {
			// this.em.persist(entity);
			// }
		}

		return entities;

	}

	/**
	 * 传入的对象包括所有的字段,所以不用重新查询,如果页面不完整，在具体的entity中实现属性替换
	 * 
	 * @param <E>
	 * @param entity
	 * @return
	 */
	protected <E extends JpaBaseModel> E update(E entity) {
		return em.merge(entity);
	}

	protected <E extends JpaBaseModel> List<E> update(List<E> entities) {
		if (CommonUtil.isNotNull(entities)) {
			for (E entity : entities) {
				this.update(entity);
			}
		}
		return entities;
	}

	protected void delete(List<? extends JpaBaseModel> entities) {
		if (CommonUtil.isNotNull(entities)) {
			for (Object entity : entities) {
				JpaBaseModel baseModel = (JpaBaseModel) entity;
				this.delete(baseModel);
			}
		}
	}

	protected void delete(JpaBaseModel entity) {
		if (CommonUtil.isNotNull(entity)) {
			this.delete(entity.getClass(), entity.getId(), entity.getVersion());
		}

	}

	/**
	 * 用保存，是以ID是否为null 或者0 为依据，决定是做新增或者修改
	 * 
	 * @param <T>
	 * @param entities
	 * @return
	 */
	protected <T extends JpaBaseModel> List<T> save(List<T> entities) {
		if (!entities.isEmpty()) {
			if (this.isManualIdEntity(entities.get(0).getClass())) {
				List<T> createList = new ArrayList<T>();
				for (T entity : entities) {
					if (entity.getId() == null || entity.getId() == 0) {
						createList.add(entity);
					} else {
						update(entity);
					}
				}
				if (CommonUtil.isNotNull(createList)) {
					this.createManualIdEntities(createList);
				}
			} else {
				for (T entity : entities) {
					if (entity.getId() == null || entity.getId() == 0) {
						em.persist(entity);
					} else {
						update(entity);
					}
				}
			}

		}
		return entities;
	}

	public <E extends JpaBaseModel> E save(E entity) {
		if (entity.getId() == null || entity.getId() == 0) {
			entity.setId(null);
			return this.create(entity);
		} else {
			return this.update(entity);
		}
	}

	public <E> List<E> findAll(Class<E> clazz) throws DataAccessException {
		return find("from " + clazz.getName(), (Object[]) null);
	}

	public List find(String queryString) throws DataAccessException {
		return find(queryString, (Object[]) null);
	}

	public List find(final String queryString, final Object... parameters) throws DataAccessException {
		Query query = em.createQuery(JPAUtils.replaceParWithSeq(queryString));
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i, parameters[i]);
			}
		}
		return query.getResultList(); 
	}	

	public int execute(final String queryString, final Object... values) throws DataAccessException {
		
		Query query = em.createQuery(JPAUtils.replaceParWithSeq(queryString));
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				query.setParameter(i + 1, values[i]);
			}
		}
		return query.executeUpdate(); 
		 
	}

	public boolean exists(final String queryString, final Object... values) throws DataAccessException {
		List list = this.find(queryString, values);
		if (list == null || list.size() == 0 ) {
			return false;
		} 
		return true;
	}

	public boolean existsNativeQuery(String queryStr, Object... parameters) {
		/**
		 * for mySql
		 */
		String tmQuery = " select 1 from dual where exists ( " + queryStr + " )";
		Query query = em.createNativeQuery(tmQuery);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i + 1, parameters[i]);
			}
		}
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	public boolean existsNativeQuery(String[] queryStrs, Object... parameters) {
		for (String queryStr : queryStrs) {
			if (existsNativeQuery(queryStr, parameters)) {
				return true;
			}
		}
		return false;
	}

	public List findByNamedParams(final String queryString, final Map<String, ?> params) throws DataAccessException {
		Query query = em.createNamedQuery(queryString);
		if (CommonUtil.isNotNull(params)) {
			for (String name : params.keySet() ) {
				query.setParameter(name, params.get(name));
			}
		} 
		return query.getResultList();
	}

	public List findByNamedQuery(String queryName) throws DataAccessException {
		return findByNamedQuery(queryName, (Object[]) null);
	}

	public List findByNamedQuery(final String queryName, final Object... values) throws DataAccessException {
		Query query = em.createNamedQuery(queryName);
		if (CommonUtil.isNotNull(values)) {
			int i=0;
			for (Object value : values ) {
				query. setParameter(i, value);
				i++;
			}
		} 
		return query.getResultList();
	}

 
 
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	/**
	 * 有同步锁的校验
	 * 
	 * @param clazz
	 * @param id
	 * @param version
	 */
	public <E extends JpaBaseModel> E getByIdAndVerion(Class<? extends JpaBaseModel> clazz, Long id, long version) {
		JpaBaseModel oldBaseModel = (JpaBaseModel) em.find(clazz, id);
		if (oldBaseModel == null) {
			throw new DllException(DllException.DELETED_BY_OTHERS);
		}
		if (version != oldBaseModel.getVersion()) {
			throw new DllException(DllException.UPDATED_BY_OTHERS);
		}
		return (E) oldBaseModel;
	}

	// 结束 以上内容从JpaDataAccessObject复制 改为不继承 为了使用两个EntityManager
	public void delete(Class<? extends JpaBaseModel> clazz, Long id, long version) {
		JpaBaseModel oldBaseModel = (JpaBaseModel) em.find(clazz, id);
		if (oldBaseModel == null) {
			throw new DllException(DllException.DELETED_BY_OTHERS);
		}
		if (version != oldBaseModel.getVersion()) {
			throw new DllException(DllException.UPDATED_BY_OTHERS);
		}
		em.remove(oldBaseModel);
	}

	public void delete(Class<? extends JpaBaseModel> clazz, Long[] ids, long[] versions) {
		for (int i = 0; i < ids.length; i++) {
			delete(clazz, ids[i], versions[i]);
		}
	}

	/**
	 * use named parameter, 例如： select ... ... where item_name = :itemName
	 */
	@SuppressWarnings("unchecked")
	public boolean existsNativeQueryByCriteria(String queryStr, Object criteria) {
		/**
		 * for mysql
		 */
		String tmQuery = " select 1 from dual where exists ( " + queryStr + " )";
		List list = findByNativeQuery(tmQuery, criteria);
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public List findByNativeQuery(String queryStr, Object criteria) {
		QueryModel qm = CommonUtil.parseQueryParameters(criteria, queryStr);
		Query query = em.createNativeQuery(qm.getQuery());
		if (qm.getParameters() != null) {
			for (int i = 0; i < qm.getParameters().length; i++) {
				query.setParameter(i + 1, qm.getParameters()[i]);
			}
		}
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List findByCriteria(final String queryString, Object criteria) throws DataAccessException {
		QueryModel qm = CommonUtil.parseQueryParameters(criteria, queryString);
		return this.findByNamedQuery(qm.getQuery(), qm.getParameters());
	}

	public <E extends JpaBaseModel> E updateProperties(E entity, String... properties) {
		JpaBaseModel oldBaseModel = this.getByIdAndVerion(entity.getClass(), entity.getId(), entity.getVersion());
		for (String key : properties) {
			YqBeanUtil.setProperty(oldBaseModel, key, YqBeanUtil.getPropertyValue(entity, key));
		}
		return em.merge((E) oldBaseModel);
	}

	public <E extends JpaBaseModel> E updateProperties(E entity) {
		String[] nonNullProperties = UpdateUtil.getNonNullProperties(entity);
		JpaBaseModel oldBaseModel = this.getByIdAndVerion(entity.getClass(), entity.getId(), entity.getVersion());
		for (String key : nonNullProperties) {
			YqBeanUtil.setProperty(oldBaseModel, key, YqBeanUtil.getPropertyValue(entity, key));
		}
		return em.merge((E) oldBaseModel);
	}

	/**
	 * 返回第一行第一列的值，主要为返回单个结果用
	 * 
	 * @param queryStr
	 * @param parameters
	 * @return
	 */
	public Object getValueByNativeQuery(String queryStr, Object... parameters) {
		String tmQuery = queryStr;
		Query query = em.createNativeQuery(tmQuery);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i+1 , parameters[i]);
			}
		}
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Object getSingleRecord(String queryStr, Object... parameters) {
		String tmQuery = queryStr;
		Query query = em.createQuery(JPAUtils.replaceParWithSeq(tmQuery));
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i , parameters[i]);
			}
		}
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Object> getListValueByNativeQuery(String queryStr, Object... parameters) {
		String tmQuery = queryStr;
		Query query = em.createNativeQuery(tmQuery);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i + 1, parameters[i]);
			}
		}
		return query.getResultList();
	}

	public Object getSingleValueByNativeQuery(String queryStr, Object... parameters) {
		String tmQuery = queryStr;
		Query query = em.createNativeQuery(tmQuery);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i + 1, parameters[i]);
			}
		}
		return query.getSingleResult();
	}

	public int executeNativeQuery(String queryStr, Object... parameters) {
		String tmQuery = queryStr;
		Query query = em.createNativeQuery(tmQuery);
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				query.setParameter(i + 1, parameters[i]);
			}
		}
		int result = query.executeUpdate();
		return result;
	}

	public JpaBaseModel auditCreate(JpaBaseModel entity) {
		JpaBaseModel result = this.create(entity);
		AuditEvent audit = new AuditEvent(entity.getClass().getSimpleName(), String.valueOf(entity.getId()),
				AuditEvent.ACTION_CREATE, null, null, entity);
		ApplicationEventPublishers.afterTransaction().publishEvent(audit);
		return result;
	}

	public JpaBaseModel auditUpdate(JpaBaseModel entity) {
		JpaBaseModel original = getById(entity.getClass(), entity.getId());
		this.em.detach(original);
		JpaBaseModel result = this.update(entity);
		AuditEvent audit = new AuditEvent(entity.getClass().getSimpleName(), String.valueOf(entity.getId()),
				AuditEvent.ACTION_UPDATE, null, original, entity);
		ApplicationEventPublishers.afterTransaction().publishEvent(audit);
		return result;
	}

	public <E> E getDetachEntityById(Class<E> clazz, Serializable id) {
		E original = getById(clazz, id);
		this.em.detach(original);
		return original;
	}

	public void auditDelete(Class clazz, Serializable id, long version) {
		JpaBaseModel entity = (JpaBaseModel) getById(clazz, id);
		if (entity == null)
			return;
		AuditEvent audit = new AuditEvent(clazz.getSimpleName(), String.valueOf(id), AuditEvent.ACTION_DELETE, null,
				entity, null);
		this.delete(entity);
		ApplicationEventPublishers.afterTransaction().publishEvent(audit);
		return;
	}

	public void auditDelete(List<? extends JpaBaseModel> entities) {
		for (Object entity : entities) {
			JpaBaseModel baseModel = (JpaBaseModel) entity;
			auditDelete(entity.getClass(), baseModel.getId(), baseModel.getVersion());
		}
	}

	public void auditDelete(JpaBaseModel entity) {
		AuditEvent audit = new AuditEvent(entity.getClass().getSimpleName(), String.valueOf(entity.getId()),
				AuditEvent.ACTION_DELETE, null, entity, null);
		this.delete(entity);
		ApplicationEventPublishers.afterTransaction().publishEvent(audit);
		return;
	}

	public List<? extends JpaBaseModel> auditSave(List<? extends JpaBaseModel> entities) {
		if (!entities.isEmpty()) {
			List<JpaBaseModel> newEntities = new ArrayList<JpaBaseModel>();
			for (Object entity : entities) {
				JpaBaseModel baseModel = (JpaBaseModel) entity;
				if (baseModel.getId() == null || baseModel.getId() == 0) {
					newEntities.add(auditCreate(baseModel));
				} else {
					newEntities.add(auditUpdate(baseModel));
				}
			}
			return newEntities;
		}
		return null;
	}

	public JpaBaseModel auditSave(JpaBaseModel entity) {
		JpaBaseModel baseModel = (JpaBaseModel) entity;
		if (baseModel.getId() == null || baseModel.getId() == 0) {
			baseModel.setId(null);
			return auditCreate(baseModel);
		} else {
			return auditUpdate(baseModel);
		}
	}

	protected List<? extends JpaBaseModel> saveLines(List<? extends JpaBaseModel> newEntities, List<? extends JpaBaseModel> oldEntities) {
		if (CommonUtil.isNotNull(oldEntities)) {
			Set<Long> idSet = new HashSet<Long>();
			if (CommonUtil.isNotNull(newEntities)) {
				for (JpaBaseModel bm : newEntities) {
					if (bm.getId() != null && bm.getId() != 0) {
						idSet.add(bm.getId());
					}
				}
			}
			for (JpaBaseModel bm : oldEntities) {
				if (!idSet.contains(bm.getId())) {
					this.delete(bm);
				}
			}
		}
		if (CommonUtil.isNotNull(newEntities)) {
			return this.save(newEntities);
		}
		return null;

	}

	protected <E extends JpaBaseModel> void saveLines(List<? extends JpaBaseModel> newEntities, String headerIdName,
			Long headerId, Class<E> clazz) {

		List<? extends JpaBaseModel> oldList = this.find(" from " + clazz.getName() + " where " + headerIdName + " = ?0",
				headerId);
		if (CommonUtil.isNotNull(newEntities)) {
			for (JpaBaseModel bm : newEntities) {
				YqBeanUtil.setProperty(bm, headerIdName, headerId);
			}
		}
		this.saveLines(newEntities, oldList);
	}

	protected <E extends JpaBaseModel> E getMasterDetailByMasterId(Class<E> clazz, Serializable id) {
		E masterBm = this.getById(clazz, id);
		List<Field> fields = YqBeanUtil.getFieldsByAnnotation(clazz, Detail.class);
		if (CommonUtil.isNotNull(fields)) {
			for (Field f : fields) {
				String masterIdProperty = f.getAnnotation(Detail.class).masterProperty();
				String detailModelName = this.genDetailModelName(f);
				List<JpaBaseModel> details = this
						.find(" from " + this.genDetailModelName(f) + " where " + masterIdProperty + "= ? ", id);
				YqBeanUtil.setProperty(masterBm, f.getName(), details);
			}
		}
		return masterBm;
	}

	/**
	 * 
	 * @param entity,包括明细，明细的属性一定是List<明细entity>，并且有@Detail注解
	 * @return
	 */
	protected <E extends JpaBaseModel> E saveMasterDetailEntity(E entity) {
		E result = entity;
		if (entity.getId() == null || entity.getId() == 0l) {
			result = this.create(entity);
		} else {
			result = this.update(entity);
		}
		List<Field> fields = YqBeanUtil.getFieldsByAnnotation(entity.getClass(), Detail.class);
		if (CommonUtil.isNotNull(fields)) {
			for (Field f : fields) {
				List<JpaBaseModel> newDetails = (List<JpaBaseModel>) YqBeanUtil.getPropertyValue(entity, f.getName());
				String masterIdProperty = f.getAnnotation(Detail.class).masterProperty();
				String detailModelName = this.genDetailModelName(f);
				this.saveDetail(newDetails, masterIdProperty, detailModelName, entity.getId());
			}
		}
		return result;
	}

	protected <E extends JpaBaseModel> void deleteMasterDetailEntity(E entity) {
		List<Field> fields = YqBeanUtil.getFieldsByAnnotation(entity.getClass(), Detail.class);
		if (CommonUtil.isNotNull(fields)) {
			for (Field f : fields) {
				String masterIdProperty = f.getAnnotation(Detail.class).masterProperty();
				String detailModelName = this.genDetailModelName(f);
				List<JpaBaseModel> details = this.find(
						" from " + this.genDetailModelName(f) + " where " + masterIdProperty + "= ? ", entity.getId());
				this.delete(details);
			}
		}
		this.delete(entity);
	}

	protected <E extends JpaBaseModel> void deleteMasterDetailEntity(Class<? extends JpaBaseModel> clazz, Long id,
			long version) {
		E entity = this.getByIdAndVerion(clazz, id, version);
		this.deleteMasterDetailEntity(entity);
	}

	/**
	 * 
	 * @param entity,包括明细，明细的属性一定是List<明细entity>，并且有@Detail注解
	 * @return
	 */
	protected JpaBaseModel auditSaveMasterDetailEntity(JpaBaseModel entity) {
		AuditEvent audit = null;
		JpaBaseModel result = entity;
		if (entity.getId() == null || entity.getId() == 0l) {
			result = this.create(entity);
			audit = new AuditEvent(entity.getClass().getSimpleName(), String.valueOf(entity.getId()),
					AuditEvent.ACTION_CREATE, null, null, entity);
		} else {
			JpaBaseModel original = getById(entity.getClass(), entity.getId());
			this.em.detach(original);
			result = this.update(entity);
			audit = new AuditEvent(entity.getClass().getSimpleName(), String.valueOf(entity.getId()),
					AuditEvent.ACTION_UPDATE, null, original, entity);
		}
		List<Field> fields = YqBeanUtil.getFieldsByAnnotation(entity.getClass(), Detail.class);
		List<AuditItem> auditItemList = new ArrayList<AuditItem>();
		if (CommonUtil.isNotNull(fields)) {
			for (Field f : fields) {
				List<JpaBaseModel> newDetails = (List<JpaBaseModel>) YqBeanUtil.getPropertyValue(entity, f.getName());
				String masterIdProperty = f.getAnnotation(Detail.class).masterProperty();
				String detailModelName = this.genDetailModelName(f);
				auditItemList
						.addAll(this.auditSaveDetail(newDetails, masterIdProperty, detailModelName, entity.getId()));
			}
		}
		audit.setItems(auditItemList);
		ApplicationEventPublishers.afterTransaction().publishEvent(audit);
		return result;
	}

	private String genDetailModelName(Field field) {
		String s = field.getGenericType().toString();
		if (!s.contains("<")) {
			throw new ValidateException("必须列表必须指定明细model名称!");
		}
		String modelName = s.substring(s.indexOf("<") + 1, s.length() - 1);
		return modelName;
	}

	/**
	 * 返回保存的明细修改日志
	 * 
	 * @return
	 */
	private List<AuditItem> auditSaveDetail(List<JpaBaseModel> newDetails, String masterIdProperty, String modelName,
			long masterId) {
		List<AuditItem> auditItemList = new ArrayList<AuditItem>();
		List<JpaBaseModel> oldDetails = this.find(" from " + modelName + " where " + masterIdProperty + "=?", masterId);
		if (CommonUtil.isNotNull(oldDetails)) {
			for (JpaBaseModel bm : oldDetails) {
				this.em.detach(bm);
			}
		}
		Map<Long, JpaBaseModel> oldMap = CommonUtil.ListToMap(oldDetails, "id");
		if (CommonUtil.isNotNull(newDetails)) {
			for (JpaBaseModel bm : newDetails) {
				if (bm.getId() == null && bm.getId() == 0l) {
					YqBeanUtil.setProperty(bm, masterIdProperty, masterId);
					JpaBaseModel result = this.create(bm);
					auditItemList.add(
							new AuditItem(result.getId().toString(), AuditEvent.ACTION_CREATE, null, null, result));
				} else {
					JpaBaseModel oldBM = oldMap.get(bm.getId());
					if (oldBM != null) {
						if (JPAUtils.modelDataIsChanged(bm, oldBM)) {
							JpaBaseModel result = this.update(bm);
							auditItemList.add(new AuditItem(result.getId().toString(), AuditEvent.ACTION_UPDATE, null,
									oldBM, result));
						}
						oldMap.remove(bm.getId());
					} else {
						JpaBaseModel result = this.update(bm);
						auditItemList.add(
								new AuditItem(result.getId().toString(), AuditEvent.ACTION_CREATE, null, null, result));
					}
				}
			}
		}
		// 非空，剩下的都要被删除
		if (CommonUtil.isNotNull(oldMap)) {
			Iterator it = oldMap.keySet().iterator();
			while (it.hasNext()) {
				Long key = (Long) it.next();
				JpaBaseModel delBM = oldMap.get(key);
				this.delete(delBM);
				auditItemList.add(new AuditItem(delBM.getId().toString(), AuditEvent.ACTION_DELETE, null, delBM, null));
			}
		}
		return auditItemList;
	}

	protected void saveDetail(List<? extends JpaBaseModel> newDetails, String masterIdProperty, String modelName,
			long masterId) {
		List<JpaBaseModel> oldDetails = this.find(" from " + modelName + " where " + masterIdProperty + "=?", masterId);
		Map<Long, JpaBaseModel> oldMap = CommonUtil.ListToMap(oldDetails, "id");
		if (CommonUtil.isNotNull(newDetails)) {
			List<JpaBaseModel> createList = new ArrayList<JpaBaseModel>();
			for (JpaBaseModel bm : newDetails) {
				if (bm.getId() == null || bm.getId() == 0l) {
					YqBeanUtil.setProperty(bm, masterIdProperty, masterId);
					createList.add(bm); // JpaBaseModel result =this.create(bm);
				} else {
					JpaBaseModel oldBM = oldMap.get(bm.getId());
					if (oldBM != null) {
						if (JPAUtils.modelDataIsChanged(bm, oldBM)) {
							JpaBaseModel result = this.update(bm);
						}
						oldMap.remove(bm.getId());
					} else {
						JpaBaseModel result = this.update(bm);
					}
				}
			}
			if (CommonUtil.isNotNull(createList)) {
				this.create(createList);
			}
		}
		// 非空，剩下的都要被删除
		if (CommonUtil.isNotNull(oldMap)) {
			Iterator it = oldMap.keySet().iterator();
			while (it.hasNext()) {
				Long key = (Long) it.next();
				JpaBaseModel delBM = oldMap.get(key);
				this.delete(delBM);
			}
		}
	}

	/**
	 * 讲数据已到历史表,数据从原表删除，保持到新表中 约定： 1. 数据的历史表一定是原来的表加"_HISTORY"后缀, 2.
	 * 为了还能从历史表转移回原表，模型一定是手动指定ID
	 * 
	 * @param entities
	 */
	protected void moveToHistory(List<? extends JpaBaseModel> entities) {
		if (CommonUtil.isNotNull(entities)) {
			for (JpaBaseModel bm : entities) {
				this.insertToHistory(bm);
				this.delete(bm);
			}
		}
	}

	protected void insertToHistory(JpaBaseModel entity) {
		Table table = entity.getClass().getAnnotation(Table.class);
		List<PropertyDefine> pdList = YqBeanUtil.genEntityDefine(entity.getClass());
		List pars = new ArrayList();
		String insertStr = "insert into " + table.name() + "_history ( ";
		String values = "";
		boolean first = true;
		for (PropertyDefine pd : pdList) {
			if (first) {
				insertStr = insertStr + pd.getColumn();
				values = "?";
				first = false;
			} else {
				values = values + ", ?";
				insertStr = insertStr + "," + pd.getColumn();
			}
			pars.add(YqBeanUtil.getPropertyValue(entity, pd.getProperty()));

		}
		insertStr = insertStr + ") values ( " + values + " )";
		this.executeNativeQuery(insertStr, pars.toArray());
	}

	protected <E extends JpaBaseModel> List<E> batchCreate(List<E> entities) {
		return this.batchCreate(entities, null);
	}

	protected int executeBatchInsert(InsertSql insertSql, Integer batchSize) {
		if (insertSql == null || CommonUtil.isNull(insertSql.getValuesList())) {
			return 0;
		}
		if (batchSize == null || batchSize == 0) {
			batchSize = 400;
		}
		int size = insertSql.getValuesList().size();
		Table table = insertSql.getClazz().getAnnotation(Table.class);
		if (JpaBaseModel.class.isAssignableFrom(insertSql.getClazz())) {
			Long startId = this.idGenerator.idNext(insertSql.getClazz(), insertSql.getValuesList().size());
			// insertSql.appendCol(JPAUtils.genBaseCol()+",id");
			for (int i = 0; i < size; i++) {
				Long id = startId + i;
				String values = insertSql.getValuesList().get(i) + "," + JPAUtils.genBaseValues() + "," + id;
				insertSql.getValuesList().set(i, values);
			}
		}
		String insertQuery = "insert into " + table.name() + " (" + insertSql.getCols() + "," + JPAUtils.genBaseCol()
				+ ",id) values ";
		int fromIndex = 0;
		boolean existsInsert = true;
		while (existsInsert) {
			String valuesArr = null;
			List<String> insertList = subList(insertSql.getValuesList(), fromIndex, batchSize);

			if (CommonUtil.isNotNull(insertList)) {
				fromIndex = fromIndex + batchSize;
				for (String values : insertList) {
					if (valuesArr == null) {
						valuesArr = "(" + values + ")";
					} else {
						valuesArr = valuesArr + ",(" + values + ")";
					}
				}
				Query query = em.createNativeQuery(insertQuery + valuesArr);
				int result = query.executeUpdate();
			} else {
				existsInsert = false;
			}
		}

		return size;
	}

	protected <E extends JpaBaseModel> List<E> batchCreate(List<E> entities, Integer batchSize) {
		return batchCreate(entities, batchSize, true);
	}

	protected <E extends JpaBaseModel> List<E> batchCreate(List<E> entities, Integer batchSize, boolean autoGenId) {
		if (batchSize == null || batchSize == 0) {
			batchSize = 200;
		}
		if (CommonUtil.isNull(entities)) {
			return entities;
		}
		Table table = entities.get(0).getClass().getAnnotation(Table.class);
		if (autoGenId) {
			if (!this.isManualIdEntity(entities.get(0).getClass())) {
				throw new ValidateException("有定义ID生成策略的，不可用createManualIdEntities（） 方法创建记录!");
			}
			if (entities.get(0).getId() == null || entities.get(0).getId() == 0l) { // 程序未指定ID,新增批量指定ID

				Long startId = this.idGenerator.idNext(entities.get(0).getClass(), entities.size());
				for (int i = 0; i < entities.size(); i++) {
					E entity = entities.get(i);
					entity.setId(startId + i);
					entity.prePersist();
					// this.em.persist(entity);
				}
			}
		}
		boolean existsInsert = true;
		List<PropertyDefine> pdlist = YqBeanUtil.genEntityDefine(entities.get(0).getClass());
		String cols = "";
		String values = "";
		for (PropertyDefine pd : pdlist) {
			cols = cols + "," + pd.getColumn();
			values = values + ",?";
		}
		cols = cols.substring(1);
		values = " (" + values.substring(1) + ") ";
		int fromIndex = 0;
		while (existsInsert) {
			String insertQuery = "insert into " + table.name() + " (" + cols + ") values ";
			List<E> insertList = subList(entities, fromIndex, batchSize);
			if (CommonUtil.isNotNull(insertList)) {
				fromIndex = fromIndex + batchSize;
				Object[] parameter = new Object[pdlist.size() * insertList.size()];
				int ind = 0;
				for (E enty : insertList) {
					if (ind == 0) {
						insertQuery = insertQuery + values;
					} else {
						insertQuery = insertQuery + "," + values;
					}
					for (PropertyDefine pd : pdlist) {
						parameter[ind] = YqBeanUtil.getPropertyValue(enty, pd.getProperty());
						ind++;
					}
				}
				this.executeNativeQuery(insertQuery, parameter);
			} else {
				existsInsert = false;
			}
		}

		return entities;
	}

	private List subList(List entities, int fromIndex, int step) {
		if (CommonUtil.isNull(entities)) {
			return null;
		}
		if (fromIndex >= entities.size()) {
			return null;
		}
		int toInd = fromIndex + step;
		if (toInd > entities.size()) {
			toInd = entities.size();
		}
		return entities.subList(fromIndex, toInd);

	}

	private <E extends JpaBaseModel> String genBatchCreateSql(List<E> entities) {
		return null;
	}

	public void delete(Class<? extends JpaBaseModel> clazz, List<IdAndVersion> idvs) {
		for (IdAndVersion iv : idvs) {
			delete(clazz, iv.getId(), iv.getVersion());
		}
	}

	public void deleteById(Class clazz, Long id) {
		JpaBaseModel oldBaseModel = (JpaBaseModel) em.find(clazz, id);
		if (oldBaseModel != null) {
			em.remove(oldBaseModel);
		}
	}

	/**
	 * 只有表名，
	 * 
	 * @param tableName
	 * @param idvList
	 */
	public void nativeDeleteByIdList(String tableName, List<IdAndVersion> idvList) {
		if (CommonUtil.isNull(idvList)) {
			return;
		}
		String inStr = "";
		for (IdAndVersion idv : idvList) {
			inStr = inStr + "," + idv.getId();
		}
		inStr = inStr.substring(1);
		String deleteSql = " delete from " + tableName + " where id in (" + inStr + ") ";
		this.executeNativeQuery(deleteSql);
	}



}