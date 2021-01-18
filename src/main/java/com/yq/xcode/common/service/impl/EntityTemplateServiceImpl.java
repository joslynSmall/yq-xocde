package com.yq.xcode.common.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.query.Jpa21Utils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.audit.jpa.service.SequenceGenerator;
import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.ParseStrModel;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.EntityTemplateCriteria;
import com.yq.xcode.common.model.EntityTemplate;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.CommonContentService;
import com.yq.xcode.common.service.ConvertService;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.LookupCodeService;
import com.yq.xcode.common.service.PageTagService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.YqBeanUtil;
import com.yq.xcode.common.view.EntityTemplateView;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@Service("EntityTemplateService")
public class EntityTemplateServiceImpl extends YqJpaDataAccessObject  implements EntityTemplateService {
    private static final String CURRENT_VALUE = "{currentValue}";
	@Autowired
	private SqlToModelService sqlToModelService; 
	@Autowired
	private ConvertService convertService;
	@Autowired
	private CommonContentService entityContentService;
	@Autowired
	private LookupCodeService lookupCodeService;
	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private PageTagService pageTagService;
	
	@Override
	public EntityTemplate saveEntityTemplate(EntityTemplate entityTemplate) {
		if(CommonUtil.isNullId(entityTemplate.getId())) {
			entityTemplate.setCode(genCode());
		}
		return this.save(entityTemplate);
	}
 
	private String genCode() {
		return yqSequenceService.nextTextSequenceNumber("TEMPLATE-",null,   4, "EntityTemplate");		
	}
	
	@Override
	public EntityTemplateView builderEntityTemplateView(Long id) {
		EntityTemplateView view = new EntityTemplateView();
		EntityTemplate entityTemplate = null;
		List<PageTag> pageTagList = null;
		if(CommonUtil.isNullId(id)) {
			  entityTemplate = initEntityTemplate(id);
		}else {
			  entityTemplate = getEntityTemplateById(id);
			  pageTagList = pageTagService.findPageTagList("TEMPLATE",String.valueOf(entityTemplate.getId()));
		}
		view.setEntityTemplate(entityTemplate);
		view.setPageTagList(pageTagList);
		return view;
	}
	
	@Override
	public Page<EntityTemplate> findEntityTemplatePage( EntityTemplateCriteria criteria) {
		String query = "select "+JPAUtils.genEntityCols(EntityTemplate.class, "a", null)
		+" from yq_entity_template a "
		+" where 1=1 ";
		return sqlToModelService.executeNativeQueryForPage(query, 
				null, null, criteria,  EntityTemplate.class);
	}
	
	@Override
	public List<EntityTemplate> findEntityTemplateList(EntityTemplateCriteria criteria) {
		String query = "select "+JPAUtils.genEntityCols(EntityTemplate.class, "a", null)
		+" from yq_entity_template a "
		+" where 1=1 ";
		return sqlToModelService.executeNativeQuery(query, null, null, criteria, EntityTemplate.class);
	}

	
	private String getDefaultWidth(String tagKey) {
 
		return null; 
	}
	
	 

	/**
	 * #cvt.dateToStr({currentValue})
	 */
	@Override
	public String replaceEntityContent(Long entityTemplateId, String entityContent,Object bean) {
		String str = entityContent;
		Map vMap = new HashMap<String,Object>();
 		vMap.put("cvt", this.convertService);
 		String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
    	Map<String,String> convertMap = this.genConvertMap(entityTemplateId);
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
    	simpleContext.setRootObject(bean); 
    	simpleContext.setVariables(vMap);  
    	ExpressionParser parser= new SpelExpressionParser();   
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return psmStr;
    		} else { 
    			String pStr = psm.getCurrStr().trim();
    			String cvtF = convertMap.get(pStr);
    			if (CommonUtil.isNotNull(cvtF)) {
    				pStr = cvtF.replace(CURRENT_VALUE, pStr); //  replace currentvalue
    			}
    			Expression exp=parser.parseExpression(pStr);
    			Object eStr = exp.getValue(simpleContext);
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr.toString();
    			}
    			psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
    		}
    	}
 		
	}
	
	private Map<String,String> genConvertMap(Long entityTemplateId) {
		Map<String,String> map = new HashMap<String,String>();
//		List<EntityExtDefine> list = this.entityExtDefineService.getEntityExtDefineList(entityTemplateId);
//		if (list != null) {
//			for (EntityExtDefine ed : list) {
//				if (CommonUtil.isNotNull(ed.getConvertFunction())) {
//					map.put(ed.getProperty(), ed.getConvertFunction());
//				}
//			}
//		}
		return map;
	}
	
	@Override
	public EntityTemplate initEntityTemplate(Long id) {
		EntityTemplate v = new EntityTemplate();
		v.setId(0L);
		v.setCategory("TEMPLATE");
		v.setTemplateStatus(true);
		return v;
	}

	@Override
	public EntityTemplate getEntityTemplateById(Long id) {
		return this.getById(EntityTemplate.class, id);
	}
	
	@Override
	public EntityTemplate getEntityTemplateByCategory(String category) {
		List<EntityTemplate> list=this.find("from EntityTemplate a where a.category=?",category);
		if(list.isEmpty()){
			return null;
		}
		return list.get(0);
	}
         
	
	@Override
	public void deleteTemplateList(List<IdAndVersion> idvs) {
		for(IdAndVersion idv:idvs) {
			deleteEntityTemplate(idv.getId());
		}
		   
	}
	
	@Override
	public void deleteEntityTemplate(Long id) {
		this.deleteById(EntityTemplate.class, id); 
	}
	
	@Override
   public EntityTemplate getEntityTemplateByCode(String code) {
	   StringBuffer query = new StringBuffer();
	   query.append(" select ")
	   .append(JPAUtils.genEntityCols(EntityTemplate.class,"a"))
	   .append(" from yq_entity_template a ")
	   .append(" where a.code ='"+JPAUtils.toPar(code)+"' ");
	     return this.sqlToModelService.getSingleRecord(query.toString(),"", EntityTemplate.class);
	   
   }
	
	
	
	
	
	
	
}
