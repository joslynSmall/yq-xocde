package com.yq.xcode.common.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.WorkFlowEntityIntf;
import com.yq.xcode.common.bean.WorkFlowEntitySkipPageView;
import com.yq.xcode.common.model.WorkFlowEntity;
import com.yq.xcode.common.model.WorkFlowSkipPageDefine;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowActionService;
import com.yq.xcode.common.service.WorkFlowSkipPageService;
import com.yq.xcode.common.utils.YqBeanUtil;

@Service("WorkFlowSkipPageService")
public class WorkFlowSkipPageServiceImpl extends YqJpaDataAccessObject implements WorkFlowSkipPageService {

	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private WorkFlowActionService workFlowActionService;
	@Autowired
	private LookupCodeService lookupCodeService;
	
	@Autowired
	private EntityTemplateService entityTemplateService;
 

	@Override
	public WorkFlowSkipPageDefine getWorkFlowSkipPageDefineById(Long id) {
		 
		return this.getById(WorkFlowSkipPageDefine.class, id);
	}

	@Override
	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(Long wfEntityId, Long skipPageDefineId) {
		WorkFlowEntity entity = this.workFlowActionService.getWorkFlowEntityIncHistory(wfEntityId);
		WorkFlowEntityIntf entityIntf = this.workFlowActionService.getWorkFlowEntityIntf(entity.getEntityId(), entity.getEntityCategory());
		return getWorkFlowEntitySkipPageView(entityIntf,skipPageDefineId);
	}

	@Override
	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(WorkFlowEntityIntf entityIntf,
			Long skipPageDefineId) { 
		WorkFlowSkipPageDefine pageDefine = this.getWorkFlowSkipPageDefineById(skipPageDefineId);
 		WorkFlowEntitySkipPageView pageView = new WorkFlowEntitySkipPageView();
		pageView.setId(entityIntf.getEntityId());
		pageView.setEntityNumber(entityIntf.getEntityNumber());
		pageView.setStatusDsp(lookupCodeService.getNameByKey(entityIntf.getEntityStatus())); 
		pageView.setEntityCategory(entityIntf.getEntityCategory());
		pageView.setEntityTemplateId(pageDefine.getEntityTemplateId());
		//YqBeanUtil.mergeValue(entityIntf, pageView, entityTemplateService.getMappingToReferReflect(pageDefine.getEntityTemplateId()));
		return pageView;
	}

	@Override
	public WorkFlowEntitySkipPageView getWorkFlowEntitySkipPageView(Long entityId, String entityCategory,
			Long skipPageDefineId) { 
		WorkFlowEntityIntf entityIntf = this.workFlowActionService.getWorkFlowEntityIntf(entityId, entityCategory);
		return getWorkFlowEntitySkipPageView(entityIntf,skipPageDefineId);
	}

}
