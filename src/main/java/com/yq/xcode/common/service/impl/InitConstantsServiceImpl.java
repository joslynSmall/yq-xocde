package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.yq.xcode.annotation.WorkFlowAnnotation;
import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.bean.WfConstants;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.PageTagService;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.WorkFlowEntityService;
import com.yq.xcode.common.service.WorkFlowService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.YqJsonUtil;
import com.yq.xcode.constants.YqSelectHardcodeConstants;
import com.yq.xcode.constants.service.LookupCodeConstantsService;
import com.yq.xcode.constants.service.ParseConstantsService;
import com.yq.xcode.constants.service.SelectConstantsService;

/**
 * 相关设置在数据空中， 系统级别， 如果修改， 必须重启系统
 * @author jettie
 *
 */
@Service("InitConstantsService")
public class InitConstantsServiceImpl extends YqJpaDataAccessObject implements InitializingBean , InitConstantsService,ApplicationContextAware  {
 
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private LookupCodeService lookupCodeService; 
	@Autowired
	private PageTagService pageTagService;
	
	@Autowired
	private SelectItemService selectItemService;
	
	@Autowired
	private WorkFlowService workFlowService;
	private List<LookupCodeCategory> lookupCodeCategoryList  = new ArrayList<LookupCodeCategory>();
	private List<SelectItemDefine> selectItemDefineList = new ArrayList<SelectItemDefine>();
	private List<WorkFlowEntityCategory> workFlowEntityCategoryList  = new ArrayList<WorkFlowEntityCategory>(); 
	private List<ParseElementUse> parseElementUseList = new ArrayList<ParseElementUse>();
	
	private static final String LOOKUPCODE_PAGETAG = "lookupCode"; 
	
	private ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
		
	}

	@Override
	public List<WorkFlowEntityCategory> getEntityCategoryList() {
		if (this.workFlowEntityCategoryList == null ) {
			this.initWorkFlowEntityCategoryList();
		}
		return this.workFlowEntityCategoryList;
	}

	@Override
	public List<LookupCodeCategory> getLookupCodeCategoryList() { 
		if (this.lookupCodeCategoryList == null ) {
			this.initLookupCodeCategory();
		}
		return lookupCodeCategoryList;
	}

	@Override
	public List<ParseElementUse> getParseElementUseList() {
		// TODO Auto-generated method stub
		return this.parseElementUseList;
	}

	@Override
	public List<SelectItemDefine> getSelectItemDefineList() {
 
		return this.selectItemDefineList;
	}

	@Override
	public WorkFlowEntityCategory getEntityCategory(String categoryCode) {
		for (WorkFlowEntityCategory cate : this.workFlowEntityCategoryList ) {
			if (categoryCode.equals(cate.getCategoryCode())) {
				return cate;
			}
		}
 
		return null;
	}

	@Override
	public LookupCodeCategory getLookupCodeCategory(String categoryCode) {
		for (LookupCodeCategory cate : this.lookupCodeCategoryList ) {
			if (cate.getCategoryCode().equals(categoryCode)) {
				return cate;
			}
		}
		this.initLookupCodeCategory();
		for (LookupCodeCategory cate : this.lookupCodeCategoryList ) {
			if (cate.getCategoryCode().equals(categoryCode)) {
				return cate;
			}
		}
		return null;
	}
	
	private void initLookupCodeCategory() { 
			Map<String,LookupCodeConstantsService> serviceMap = this.applicationContext.getBeansOfType(LookupCodeConstantsService.class);
			if (CommonUtil.isNotNull(serviceMap)) {
				for (LookupCodeConstantsService service : serviceMap.values()) {
					List<LookupCodeCategory> tmpList = service.getLookupCodeCategoryList();
					if ( tmpList != null ) {
						this.lookupCodeCategoryList.addAll(tmpList);
					}
 
					
					Class[] queryArr = service.getLookupCodeConstantsArray();
					if ( queryArr != null ) {
						for (Class clazz : queryArr ) {
							tmpList = CommonUtil.genStaticDefineList(clazz, LookupCodeCategory.class);
							this.lookupCodeCategoryList.addAll(tmpList);
						}
					} 
					
				}
				
			}  
	}
 
	private void initSelectItemDefineList() {
		try {
			Map<String,SelectConstantsService> serviceMap = this.applicationContext.getBeansOfType(SelectConstantsService.class);
			if (CommonUtil.isNotNull(serviceMap)) {
				for (SelectConstantsService service : serviceMap.values()) {
					List<SelectItemDefine> tmpList = service.getSelectItemDefineList();
					if ( tmpList != null ) {
						this.selectItemDefineList.addAll(tmpList);
					}
					Class[] hardcodeArr = service.getSelectHarcodeConstansClassArr();
					if ( hardcodeArr != null ) {
						for (Class clazz : hardcodeArr ) {
							this.selectItemDefineList.addAll(CommonUtil.genSelectItemDefineList(clazz));
						}
					} 
					
					Class[] queryArr = service.getSelectQueryConstansClassArr();
					if ( queryArr != null ) {
						for (Class clazz : queryArr ) {
							tmpList = CommonUtil.genStaticDefineList(clazz, SelectItemDefine.class);
							this.selectItemDefineList.addAll(tmpList);
						}
					} 
					
				}
				
			} 
 		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initWorkFlowEntityCategoryList() {
		Map<String,WorkFlowEntityService> serviceMap = this.applicationContext.getBeansOfType(WorkFlowEntityService.class);
		if ( serviceMap != null ) {
			for (WorkFlowEntityService service : serviceMap.values() ) {
				String clazzStr = service.getClass().getName();
				clazzStr = this.cutClassStr(clazzStr);
				Class clazz = null;
				try {
					clazz = Class.forName(clazzStr);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			 
				WorkFlowAnnotation wfAnn = (WorkFlowAnnotation) clazz.getAnnotation(WorkFlowAnnotation.class);
				Service serviceAnn = (Service)clazz. getAnnotation(Service.class);
				if (wfAnn != null ) { 
					String[][]   attributeDef = null;
					if (CommonUtil.isNotNull(wfAnn.attributeDef())) {
						List<SelectItem> siList = YqJsonUtil.parseArray(wfAnn.attributeDef(), SelectItem.class);
						attributeDef = new String[siList.size()][2];
						int i = 0 ;
						for (SelectItem si : siList ) {
							attributeDef[i][0] = si.getItemKey();
							attributeDef[i][1] = si.getItemName();
						}
					} 
					WorkFlowEntityCategory cate = new WorkFlowEntityCategory(wfAnn.categoryCode(), wfAnn.categoryName(), wfAnn.openUrl(),wfAnn.wxUrl(), serviceAnn.value(),wfAnn.criteriaClass(), attributeDef);
					cate.setEntityClass(wfAnn.entityClass());
					this.workFlowEntityCategoryList.add(cate);
				}
				
			}
		}
		
		System.out.println("流程初始化成功");
		
//		String query = "select " 
//                +" a.category_code categoryCode "
//                +" , a.category_name categoryName "
//                +" , a.open_url openUrl "
//                +" , a.service_name serviceName "
//                +" , a.description description "
//                +" , a.criteria_name criteriaName "
//				+" , a.line_number lineNumber "
//				+" , a.attribute_def attributeDef "
//				+" , a.wx_url wxUrl "
//                +" from yq_work_flow_category a ";
//         this.workFlowEntityCategoryList = this.sqlToModelService.executeNativeQuery(query, null, WorkFlowEntityCategory.class);
	
	
	} 

	@Override
	public ParseElementUse getParseElementUse(String categoryCode) {
		for (ParseElementUse pu : this.parseElementUseList ) {
			if ( categoryCode.equals(pu.getCode() )) {
				return pu;
			}
		}
		return null;
	}

	@Override
	public SelectItemDefine getSelectItemDefine(String categoryCode) {
		for (SelectItemDefine def : this.selectItemDefineList ) {
			if (def.getCode().equals(categoryCode)) {
				return def;
			}
		}
 
		return null;
	}

	@Override
	public String getRealmCode() { 
		return null;
	} 
	@Override
	public void afterPropertiesSet() throws Exception {
		 
		this.initLookupCodeCategory();
		this.initSelectItemDefineList();
		this.initWorkFlowEntityCategoryList();
		this.initParseElementUseList();
		System.out.println("初始化完成 ！");
	}
	
	

	private void initParseElementUseList() {
 
		List<SelectItem> proList = new ArrayList<SelectItem>();
		proList.add(new SelectItem(WfConstants.WORKFLOW_RELATION_CONDITION,"选择流程的条件",null,YqSelectHardcodeConstants.PropertyType.BOOLEAN));
		proList.add(new SelectItem(WfConstants.WORKFLOW_DETAIL_RELATION_CONDITION,"操作条件",null,YqSelectHardcodeConstants.PropertyType.BOOLEAN));
 		proList.add(new SelectItem(WfConstants.WORKFLOW_DETAIL_EXECUTE_METHOD,"成功执行事件",null,YqSelectHardcodeConstants.PropertyType.OBJECT));
		for ( WorkFlowEntityCategory cate : this.workFlowEntityCategoryList ) {
			ParseElementUse pu = new ParseElementUse();
			this.parseElementUseList.add(pu);
			pu.setCode(cate.getCategoryCode());
			pu.setName(cate.getCategoryName());
			pu.setProperties(proList);
		}
		
		Map<String,ParseConstantsService> serviceMap = this.applicationContext.getBeansOfType(ParseConstantsService.class);
		if (CommonUtil.isNotNull(serviceMap)) {
			for (ParseConstantsService service : serviceMap.values()) {
				List<ParseElementUse> tmpList = service.getParseElementUseList();
				if ( tmpList != null ) {
					this.parseElementUseList.addAll(tmpList);
				}
				Class[] clazzArr = service.getParseCodeConstantsArray();
 				if ( clazzArr != null ) {
					for (Class clazz : clazzArr ) {
						tmpList = CommonUtil.genStaticDefineList(clazz, ParseElementUse.class);
						this.parseElementUseList.addAll(tmpList);
					}
				} 
				
			}
 		}  
	}
	
	private String cutClassStr(String str ) {
		//com.siyu.scp.service.common.impl.AccountDeliveryServiceImpl$$EnhancerBySpringCGLIB$$445665a1
		int ind = str.indexOf("$$");
		return str.substring(0,ind);
	}

 
 
 
}
