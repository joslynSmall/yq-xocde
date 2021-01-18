package com.yq.xcode.common.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.IdAndVersion;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.PageTagCriteria;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.EntityTemplate;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.common.service.EntityTemplateService;
import com.yq.xcode.common.service.PageTagService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.view.PageTagGroupView;
import com.yq.xcode.constants.YqConstants;
import com.yq.xcode.web.ui.annotation.ColumnLable;

@Service("PageTagService")
@Transactional
public class PageTagServiceImpl extends YqJpaDataAccessObject implements PageTagService {
	
 	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private EntityTemplateService entityTemplateService;
	@Override
	public List<PageTag> getPageTagList(String sourceCategory) {
		return this.getPageTagList(sourceCategory,  null);
	}
	@Override
	public List<PageTag> getPageTagList(String sourceCategory, String sourceHeaderKey) {
		 
		return this.getPageTagList(sourceCategory, sourceHeaderKey,null);
	}
	@Override
	public List<PageTag> getPageTagList(String sourceCategory, String sourceHeaderKey, String sourceLineKey) {
        String query = "select " + JPAUtils.genEntityCols(PageTag.class, "a", null)
        +" from yq_page_tag a "
        +" where a.source_category = '"+JPAUtils.toPar(sourceCategory)+"' ";
        if (CommonUtil.isNotNull(sourceHeaderKey)) {
        	query = query+" and a.source_header_key = ' "+JPAUtils.toPar(sourceHeaderKey)+ "'  ";
        }
        
        if (CommonUtil.isNotNull(sourceLineKey)) {
        	query = query+" and a.source_line_key = ' "+JPAUtils.toPar(sourceLineKey)+ "'  ";
        }
        query = query +" order by a.line_number ";
        
		return this.sqlToModelService.executeNativeQuery(query, null, PageTag.class);
	}
	
	
	@Override
	public List<PageTag> findPageTagByEntityTemplateCode(String entityTemplateCode,String groupName) {
		EntityTemplate entityTemplate = entityTemplateService.getEntityTemplateByCode(entityTemplateCode);
		if(CommonUtil.isNull(entityTemplate)) {
			throw new ValidateException("【"+entityTemplateCode+"】不存在，请检查！");
		}
		StringBuffer query = new StringBuffer();
		query.append(" select ")
		.append(JPAUtils.genEntityCols(PageTag.class, "a"))
		.append(" from yq_page_tag a ")
		.append(" where a.SOURCE_CATEGORY = '"+JPAUtils.toPar(entityTemplate.getCategory())+"' and  a.source_key = '"+JPAUtils.toPar(entityTemplate.getId())+ "' ")
		.append(" and a.group_Name='"+JPAUtils.toPar(groupName)+"' ")
		.append(" order by group_line_number, line_number   ");
		return this.sqlToModelService.executeNativeQuery(query.toString() ,"", PageTag.class);
	}
	
	@Override
	public List<PageTag> findPageTagList(PageTagCriteria criteria) {
		StringBuffer query = new StringBuffer();
		query.append(" select ")
		.append(JPAUtils.genEntityCols(PageTag.class, "a"))
		.append(" from yq_page_tag a ")
		.append(" where 1=1 ");
		return this.sqlToModelService.executeNativeQuery(query.toString(),  " group_line_number, line_number  ", "", criteria, PageTag.class);
	}
 
	@Override
	public List<PageTag> findPageTagList(String sourceCategory,String sourceKey) {
		StringBuffer query = new StringBuffer();
		query.append(" select ")
		.append(JPAUtils.genEntityCols(PageTag.class, "a"))
		.append(" from yq_page_tag a ")
		.append(" where a.SOURCE_CATEGORY = '"+JPAUtils.toPar(sourceCategory)+"' and  a.source_key = '"+JPAUtils.toPar(sourceKey)+ "' ")
		.append(" order by group_line_number, line_number   ");
		return this.sqlToModelService.executeNativeQuery(query.toString() ,"", PageTag.class);
	}
	
	private PageTag  getPageTagLineNumberDesc(String sourceCategory,String sourceKey) {
		StringBuffer query = new StringBuffer();
		query.append(" select ")
		.append(" a.line_number lineNumber ")
		.append(" from yq_page_tag a ")
		.append(" where a.SOURCE_CATEGORY = '"+JPAUtils.toPar(sourceCategory)+"' and  a.source_key = '"+JPAUtils.toPar(sourceKey)+ "' ")
		.append(" order by line_number desc limit 1 ");
		return this.sqlToModelService.getSingleRecord(query.toString() ,"", PageTag.class);
	}
	
	
	
	@Override
	public PageTag initPageTag(String sourceCategory,String sourceKey) {
		PageTag v = new PageTag();
		v.setId(0L);
		v.setSourceCategory(sourceCategory);
		v.setSourceKey(sourceKey);
		v.setLineNumber(1);
		PageTag pt = getPageTagLineNumberDesc(sourceCategory,sourceKey);
		if(CommonUtil.isNotNull(pt)) {
			v.setLineNumber(pt.getLineNumber()+3);
		}
		return v;
	}
	
	
	
	@Override
	public PageTag getPageTagById(Long id) {
	    return this.getById(PageTag.class,id);
	}
	
	@Override
	 public PageTag savePageTag(PageTag pageTag) {
		  return this.save(pageTag);
	 }
	 
	@Override
	public void deletePageTag(List<IdAndVersion> idvs) {
		 for(IdAndVersion idv:idvs) {
			 deletePageTagById(idv.getId());
		 }
	}
	
	@Override
	public void deletePageTagById(Long id) {
		  this.deleteById(PageTag.class, id);
	}
	
	 /**
     * 取模板的className 属性
     * @return
     */
	@Override
	public List<SelectItem> findEntityTemplateClassProperty(Long entityTemplateId){
		EntityTemplate et = entityTemplateService.getEntityTemplateById(entityTemplateId);
		List<SelectItem> list = new ArrayList<SelectItem>();
		String className =  et.getClassName();
		if(CommonUtil.isNull(className)) {
			return list;
		}
		try {
			Class clazz = Class.forName(className);
			Field[] fs = clazz.getDeclaredFields();  
			 
			 for(Field f:fs) {
				   ColumnLable columnLable = f.getAnnotation(ColumnLable.class);
				   if(CommonUtil.isNull(columnLable)) {
					   continue;
				   }
				   if(et.getExtPropertyDefine()) {
					   if(columnLable.extProperty()) {
						   list.add(new SelectItem(f.getName(),columnLable.name()));
					   }
				   }else {
					   list.add(new SelectItem(f.getName(),columnLable.name()));
				   }
				  
			 }
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	@Override
	public List<SelectItem> findReadTypeSource(){
		  return YqConstants.LIST_CATEGORY_HARDCODE_PREFIX.SELECT_LIST;
	}
	 
	/**
	 *  批量设置分组名称\序号
	 * @param idvs
	 * @param groupLineNumber
	 * @param groupName
	 */
	@Override
	public void  batchUpdatePageTagGroup(PageTagGroupView pageTagGroupView) {
		List<PageTag> list = new ArrayList<PageTag>();
		PageTag pt = null;
		for(IdAndVersion idv: pageTagGroupView.getIdvs()) {
			 if(CommonUtil.isNull(pageTagGroupView.getGroupName())) {
				 throw new ValidateException("分组名称不可以为空");
			 }
			 pt = this.getById(PageTag.class, idv.getId());
			 pt.setGroupLineNumber(pageTagGroupView.getGroupLineNumber());
			 pt.setGroupName(pageTagGroupView.getGroupName());
			 pt.setGroupRemark(pageTagGroupView.getGroupRemark());
			 list.add(pt);
		}
		 this.update(list);
	}
	 
	
	@Override
	public List<SelectItem> findGroupNameList(String sourceCategory,String sourceKey){
		StringBuffer query = new StringBuffer();
		query.append(" select ")
		.append(" distinct group_name itemKey, concat(group_line_number,'.',group_name,'-',ifNUll(GROUP_REMARK,'')) itemName ")
		.append(" from yq_page_tag a ")
		.append(" where a.SOURCE_CATEGORY = '"+JPAUtils.toPar(sourceCategory)+"' and  a.source_key = '"+JPAUtils.toPar(sourceKey)+ "' ")
		.append(" order by group_line_number   ");
		return this.sqlToModelService.executeNativeQuery(query.toString(), "", SelectItem.class);
	}
	 
 
	 
}