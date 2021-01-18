package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.yq.xcode.annotation.WorkFlowAnnotation;
import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.model.WorkFlowDetail;
import com.yq.xcode.common.model.WorkFlowEntityTest;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowEntityTestService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.springdata.HWorkFlowEntityPageCriteria;


@Service("WorkFlowEntityTestService")
//@WorkFlowAnnotation(categoryCode="WF-OVERTIME",categoryName="加班单",openUrl="/delivery/receiptApp_edit/",criteriaClass= HWorkFlowEntityPageCriteria.class)
public class WorkFlowEntityTestServiceImpl extends YqJpaDataAccessObject implements WorkFlowEntityTestService {
	
	@Autowired
	private WorkFlowService workFlowService;
	@Autowired
	private SqlToModelService sqlToModelService;

	@Override
	public WorkFlowEntityTest saveWorkFlowEntityTest(WorkFlowEntityTest workFlowEntityTest) {
		return this.save(workFlowEntityTest);
	}

	@Override
	public List<WorkFlowEntityIntf> findWorkFlowEntityByEntityIds(Long[] entityIds) {
		// 测试的写法，开发不可一个一个取， 要批量一起去
		List<WorkFlowEntityIntf> list = new ArrayList<WorkFlowEntityIntf>();
		for (Long id : entityIds) {
			list.add(this.getById(WorkFlowEntityTest.class, id));
		}
		return list;
	}

	@Override
	public Long getWorkFlowIdByEntity(WorkFlowEntityIntf workFlowEntity) {
		return null;
	}

	@Override
	public WorkFlowEntityIntf saveEntity(WorkFlowEntityIntf workFlowEntity,WorkFlowDetail wfd) {
		return this.saveWorkFlowEntityTest((WorkFlowEntityTest) workFlowEntity);
	}

 
	@Override
	public WorkFlowEntityIntf closedEntity(WorkFlowEntityIntf workFlowEntity,WorkFlowDetail wfd) {
		return this.saveWorkFlowEntityTest((WorkFlowEntityTest) workFlowEntity);
	}

 
	@Override
	public WorkFlowEntityIntf getWorkFlowEntityByEntityId(Long entityId) {
		return this.getById(WorkFlowEntityTest.class, entityId);
	}
 

	@Override
	public Page findEntityPage(HWorkFlowEntityPageCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getPageColList() {
		// TODO Auto-generated method stub
		return null;
	}

 

 
}
