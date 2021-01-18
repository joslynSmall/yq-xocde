package com.yq.xcode.common.service;

import java.util.List;

import com.yq.xcode.common.bean.ContractFunctionView;
import com.yq.xcode.common.bean.ParseElementDisplayView;
import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.criteria.ParseElementQueryCriteria;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.ParseElementTerms;


public interface ParseElementService {

	public List<ParseElement> findAllParseElements(ParseElementQueryCriteria criteria);
	
	public ParseElement getParseElementById(Long id);
	
	public ParseElement getParseElementByIdAndNew(Long id,String etype,String useCategory);
	
	public ParseElement editParseElement(ParseElement pe);
	
	public List<ParseElementUse> findAllCategoryWithWFCategory();
	
	public ParseElementDisplayView checkParseElementUse(ParseElement pe);
	
	public ParseElement translateParseElement(ParseElement pe);
	
	public ParseElement getParseElementByEleNumber(String eleNumber);
	
	public List<ParseElement> findParameterByElement(ParseElementQueryCriteria criteria);

//	public List<ChargeItem> findAllChargeItems(ParseElementQueryCriteria criteria);

	public List<ParseElement> findFunctionByChargeItem(String code);

	public ParseElementTerms saveParseElementTerms(ParseElementTerms parseElementTerms);
	
	public ParseElementTerms getParseElementTerms(Long id);
	
	public ContractFunctionView getParseElementTerms2FunctionView(Long id);
	
	public List<ParseElementTerms> getParseElementTerms( String modelProperty, Long modelId,String chargeCode);
	
	public ParseElementTerms getParseElementTerms(ParseElementTerms parseElementTerms,String type);
	
	public List<ContractFunctionView> getParseElementTerms2FunctionView(String modelProperty, Long modelId,String chargeCode);

	public List<ParseElement> findFunctionByUseCategory(String userCategory);

 	public List<SelectItem> getFunParaList(String funCode);
 

	public List<ParseElement> findParameterByUseCategory(String userCategory);

	public List<SelectItem> findelementGetResult(String eleNumber);

	public List<SelectItem> findfunctionByParameter(String useCategory);


}
