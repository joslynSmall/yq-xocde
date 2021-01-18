package com.yq.xcode.common.audit.jpa.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.audit.jpa.service.YqSequenceGenerator;

@Service("idGenerator")
public class IdSequenceGenerator extends YqSimpleSequenceGenerator  implements YqSequenceGenerator,InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception{ 
		
		/*
		<bean id="idGenerator"
				class="com.yunqi.commonplatform.service.system.impl.YqSimpleSequenceGenerator">
				<property name="tableName" value="ID_GENERATOR"/>
				<property name="useForUpdate" value="false"/>
				<property name="allowPreallocate" value="true"/>
				<property name="allowInterval" value="50"/>
				<property name="dataSource" ref="dataSource"/>
			</bean>
		*/
		super.setTableName("ID_GENERATOR");
		super.setUseForUpdate(false);
		super.setAllowPreallocate(true);
		super.setAllowInterval(50); 
		super.afterPropertiesSet();
	} 
	 
}