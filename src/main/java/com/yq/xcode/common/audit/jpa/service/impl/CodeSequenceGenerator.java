package com.yq.xcode.common.audit.jpa.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.audit.jpa.service.YqSequenceGenerator;

@Service("codeGenerator")
public class CodeSequenceGenerator extends YqSimpleSequenceGenerator  implements YqSequenceGenerator,InitializingBean {
 	 	
	@Override
	public void afterPropertiesSet() throws Exception{
		/*
		  <bean id="sequenceGenerator"
				class="com.yunqi.commonplatform.service.system.impl.YqSimpleSequenceGenerator">
				<property name="tableName" value="ID_GENERATOR_0"/>
				<property name="useForUpdate" value="false"/>
				<property name="allowPreallocate" value="true"/>
				<property name="allowInterval" value="1"/>
				<property name="dataSource" ref="dataSource"/>
			</bean>
			*/
		super.setTableName("ID_GENERATOR_0");
		super.setUseForUpdate(false);
		super.setAllowPreallocate(true);
		super.setAllowInterval(1);
		super.afterPropertiesSet();
	} 
	 
}