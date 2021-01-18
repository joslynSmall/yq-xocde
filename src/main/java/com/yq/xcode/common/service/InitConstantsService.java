


 package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.LookupCodeCategory;
import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.bean.SelectItemDefine;
import com.yq.xcode.common.bean.WorkFlowEntityCategory;


public interface InitConstantsService {
	

	public List<WorkFlowEntityCategory> getEntityCategoryList() ;

	public List<LookupCodeCategory> getLookupCodeCategoryList();

	public List<ParseElementUse> getParseElementUseList();
	
	public List<SelectItemDefine> getSelectItemDefineList();
	
	
	
	public WorkFlowEntityCategory getEntityCategory(String categoryCode );

	public LookupCodeCategory getLookupCodeCategory(String categoryCode );
  
	public ParseElementUse getParseElementUse(String categoryCode ) ;
	
	public SelectItemDefine getSelectItemDefine(String categoryCode);
	
	public String getRealmCode() ;

	//public List<SelectItem> getHardCodeSelectItemList(String selectUtil, String category);
 
	
	  
}
