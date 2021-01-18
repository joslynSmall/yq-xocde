package com.yq.xcode.security.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.common.bean.ResourceInstance;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.service.ResourceInstanceProvider;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.impl.YqJpaDataAccessObject;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.security.criteria.ResourceCriteria;
import com.yq.xcode.security.entity.ResourceAssignment;
import com.yq.xcode.security.entity.ResourceDefination;
import com.yq.xcode.security.service.ResourceService;

@Service
@Transactional
public class ResourceServiceImpl extends YqJpaDataAccessObject implements ResourceService ,ApplicationContextAware,InitializingBean{

	private static Log LOG = LogFactory.getLog(ResourceServiceImpl.class);
	
    @Autowired
    private SqlToModelService sqlToModelService; 
    
    private List<ResourceInstanceProvider> resourceInstanceProviders;
	
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
 
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		if(resourceInstanceProviders == null) {
			resourceInstanceProviders = new ArrayList<ResourceInstanceProvider>();
		}
		String[] providerNames = applicationContext.getBeanNamesForType(ResourceInstanceProvider.class);
		if(providerNames != null) {
			for(String name : providerNames) {
				ResourceInstanceProvider provider = applicationContext.getBean(name, ResourceInstanceProvider.class);
				resourceInstanceProviders.add(provider);
				LOG.info("ResourceInstanceProvider found, name="+name+", bean="+provider);
			}
		}
	}
    
	@Override
	public List<ResourceDefination> findAll(ResourceCriteria criteria) {
	     StringBuffer query = new StringBuffer();
	        query.append("SELECT ")
	                .append(JPAUtils.genEntityCols(ResourceDefination.class, "a",null))
	                .append(" FROM sec_resource a")
	                .append(" WHERE 1=1  ");
	        List<ResourceDefination> list = 
	        		sqlToModelService.executeNativeQuery(query.toString(),
	        				"a.code", criteria, ResourceDefination.class);
	      return list;
	}

	@Override
	public ResourceDefination getResourceDefinationById(Long id) {
	  return this.getById(ResourceDefination.class, id);
	}

	@Override
	public void saveResourceDefination(ResourceDefination resource) {
		this.save(resource);
	}

	@Override
	public void deleteResourceDefination(Long id) {
		this.deleteById(ResourceDefination.class, id);
	}

	@Override
	public Page<ResourceDefination> findResourcePage(ResourceCriteria criteria) {
       StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(ResourceDefination.class, "a",null))
                .append(" FROM sec_resource a")
                .append(" WHERE 1=1  ");
        Page<ResourceDefination> page = 
        		sqlToModelService.executeNativeQueryForPage(query.toString(),
        				"a.code", null, criteria, ResourceDefination.class);

       return page;
	}

	@Override
	public List<ResourceInstance> findResourceInstanceList(String resourceName) {
		ResourceInstanceProvider provider = findResourceInstanceProvider(resourceName);
		if(provider == null) {
			throw new ValidateException("ResourceInstanceProvider for "+resourceName+" not found.");
		}
		return provider.findAllResourceInstances();
	}
	
	protected ResourceInstanceProvider findResourceInstanceProvider(String name) {
		for(ResourceInstanceProvider provider : resourceInstanceProviders) {
			if(provider.getResourceName().equals(name)) {
				return provider;
			}
		}
		return null;
	}

	@Override
	public List<ResourceAssignment> getResourceAssignmentBySId(Long resourceDefinationId, Long sid) {
        StringBuffer query = new StringBuffer();
        query.append("SELECT ")
                .append(JPAUtils.genEntityCols(ResourceAssignment.class, "a",null))
                .append(" FROM sec_resource_assignment a  ")
                .append(" WHERE a.resource_def_id  =" + resourceDefinationId)
                .append("   AND a.sid_id = " + sid );
        List<ResourceAssignment> list = sqlToModelService.executeNativeQuery(query.toString(),
        		null, ResourceAssignment.class);
        return list;
	}

	@Override
	public ResourceDefination getResourceDefination(String resourceName) {
		 StringBuffer query = new StringBuffer();
	        query.append("SELECT ")
	                .append(JPAUtils.genEntityCols(ResourceDefination.class, "a",null))
	                .append(" FROM sec_resource a")
	                .append(" WHERE a.code =  '"+resourceName+"'");
		return sqlToModelService.getSingleRecord(query.toString(), null, ResourceDefination.class);
	}

}
