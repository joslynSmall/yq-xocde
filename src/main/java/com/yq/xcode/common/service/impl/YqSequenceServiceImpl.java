package com.yq.xcode.common.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yq.xcode.common.audit.jpa.service.impl.CodeSequenceGenerator;
import com.yq.xcode.common.audit.jpa.service.impl.IdSequenceGenerator;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.security.entity.JpaBaseModel;

@Service("YqSequenceService")
public class YqSequenceServiceImpl implements YqSequenceService{
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	@Qualifier("codeGenerator")
	private CodeSequenceGenerator codeSequenceGenerator;
 
	@Autowired
    @Qualifier("idGenerator")
	private IdSequenceGenerator idGenerator;
	
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
    public <E extends JpaBaseModel> void genId( E entity  ) {
     	entity.setId(this.idGenerator.nextSequence(JPAUtils.genManualIdKey(entity.getClass())));
    }
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public <E extends JpaBaseModel> void genId(List<E> entities) {
    	if (CommonUtil.isNotNull(entities)) {
    		Long startId = this.idGenerator.nextSequence(JPAUtils.genManualIdKey(entities.get(0).getClass()));
    		for (E entity : entities) {
    			entity.setId(startId);
    			startId ++;
    		}
    	}
    }
 
    @Override
    @Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public String nextTextSequenceNumber(String prefix,String suffix,int numberSize,String sequenceId) {
		 return this. codeSequenceGenerator.nextTextSequence(prefix, suffix, numberSize, sequenceId);
	 }
	 /**
	  * 直接批量设置sequenceNumber
	  * @param entityList
	  * @param numProperty
	  * @param prefix
	  * @param suffix
	  * @param numberSize
	  * @param sequenceId
	  */
	 @Override
	 @Transactional(propagation=Propagation.NOT_SUPPORTED)
	 public void assignSequenceNumber(List entityList, String numProperty, String prefix,String suffix,int numberSize,String sequenceId) {
		 if (CommonUtil.isNull(entityList)) {
			 return;
		 }
		 Long startId = this.codeSequenceGenerator.nextSequence(sequenceId,entityList.size());
		 for (Object bean : entityList) {
			 String number = this.unionNumber(prefix, suffix, numberSize, startId);
			 YqBeanUtil.setProperty(bean, numProperty, number);
			 startId ++;
		 }
		 
	 }
	 
	 
	 private String unionNumber(String prefix,String suffix,int numberSize,long value) {
			StringBuffer buffer = new StringBuffer();
			if(prefix != null) {
				buffer.append(prefix);
			} 
			String numberText = String.valueOf(value);
			int currentSize = numberText.length();
			if(currentSize > numberSize) {
				buffer.append(numberText.substring(currentSize - numberSize));
			}else if(currentSize < numberSize) {
				for(int i = 0;i < numberSize - currentSize;i++) {
					buffer.append('0');
				}
				buffer.append(numberText);
			}else {
				buffer.append(numberText);
			}
			if(suffix != null) {
				buffer.append(suffix);
			}
			return buffer.toString();
		}
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public <E extends JpaBaseModel> Long idNext(Class<E> clazz, int size) {
		Long startId = this.idGenerator.nextSequence(JPAUtils.genManualIdKey(clazz),size);
		return startId;
	}
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public <E extends JpaBaseModel> void addIdSequence(String sequenceId, int size) {
		 this.idGenerator.addSequence(sequenceId, size) ; 
	}
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public <E extends JpaBaseModel> void addIdSequence(Class<E> clazz, int size) {
 		this.idGenerator.addSequence(JPAUtils.genManualIdKey(clazz), size) ;
	}
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
    public Long idNext( String sequenceId  ) {
     	return this.idGenerator.nextSequence(sequenceId);
    }
	
	
	
	 
}
