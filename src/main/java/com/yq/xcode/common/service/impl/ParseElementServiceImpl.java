package com.yq.xcode.common.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.yq.xcode.common.bean.*;
import com.yq.xcode.common.model.ParseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.criteria.ParseElementQueryCriteria;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.ParseElementTerms;
import com.yq.xcode.common.service.InitConstantsService;
import com.yq.xcode.common.service.ParseElementService;
import com.yq.xcode.common.service.ParseService;
import com.yq.xcode.common.service.SelectItemService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.constants.YqParseConstants;
import org.springframework.transaction.annotation.Transactional;

@Service("ParseElementMainService")
public class ParseElementServiceImpl extends YqJpaDataAccessObject implements ParseElementService {

	@Autowired
	private SqlToModelService sqlToModelService;
	@Autowired
	private YqSequenceService yqSequenceService;
	@Autowired
	private ParseService parseService;
	@Autowired
	private SelectItemService selectItemService;
	@Autowired
	private InitConstantsService initConstantsService;
//	@Autowired
//	private ContractService contractService;

	@Override
	public List<ParseElement> findAllParseElements(ParseElementQueryCriteria criteria) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT").append(" spe.* ").append(" FROM").append(" yq_parse_element spe ").append(" where 1=1");

		if (!CommonUtil.isNull(criteria.getEleCategory())) {
			sql.append(" and spe.ELE_CATEGORY ='").append(criteria.getEleCategory()).append("'");
		}
		if (!CommonUtil.isNull(criteria.getCategoryCode())) {
			sql.append(" and spe.USE_CATEGORY ='").append(criteria.getCategoryCode()).append("'");
		}
        if(CommonUtil.isNotNull(criteria.getKeyString())) {
        	sql.append(" and concat(spe.ELE_NUMBER, spe.ELE_NAME) LIKE '%"+criteria.getKeyString()+"%'");
        }
		sql.append(" order by spe.ELE_NUMBER asc ");

		List<ParseElement> pelist = sqlToModelService.executeNativeQuery(sql.toString(), null, ParseElement.class);
		this.parasToChinese(pelist);
		if ("F".equals(criteria.getEleCategory())) {
			if (CommonUtil.isNotNull(pelist)) {
				List<SelectItem> types =  findelementUseProperty(criteria.getCategoryCode());
				Map<String, SelectItem> siMap = CommonUtil.ListToMap(types, "itemKey");
				for (ParseElement pe : pelist) {
					SelectItem si = siMap.get(pe.getUsePropertyName());
					if (si != null) {
						pe.setUsePropertyDspName(si.getItemName());
					}
				}
			}
		}

		return pelist;
	}

	private void parasToChinese(List<ParseElement> peList) {
		if (CommonUtil.isNull(peList)) {
			return;
		}
		List<ParseElement> paraList = this.find("from ParseElement where eleNumber like 'P%' ");
		Map<String, ParseElement> pMap = CommonUtil.ListToMap(paraList, "eleNumber");
		for (ParseElement pe : peList) {
			String parDsp = "";
			if (CommonUtil.isNotNull(pe.getParameters())) {
				String[] sA = pe.getParameters().split(",");
				for (String eleNumber : sA) {
					ParseElement p = pMap.get(eleNumber);
					if (p != null) {
						parDsp = parDsp + p.getEleNumber() + "-" + p.getEleName() + ",</br>";
					}
				}
			}
			pe.setParametersDsp(parDsp);
		}
	}

	@Override
	public ParseElement getParseElementById(Long id) {

		return this.getById(ParseElement.class, id);
	}

	@Override
	public ParseElement getParseElementByIdAndNew(Long id, String etype, String useCategory) {

		ParseElement pe = new ParseElement();
		if (id != 0L) {
			pe = this.getParseElementById(id);
			this.translateParseElement(pe);

		} else {
			pe.setId(0L);
			pe.setUseCategory(useCategory);
			pe.setEleCategory(etype);
			pe.setSystem(false);
			pe.setDeleted(false);
		}
		return pe;
	}

	@Override
	public ParseElement editParseElement(ParseElement pe) {
		ParseElement pesave = new ParseElement();
		if (pe.getId() == 0L) {
			pe.setId(null);
			if (("P").equals(pe.getEleCategory())) {
				String eleNumber = this.yqSequenceService.nextTextSequenceNumber("P", null, 3, "ELEMENT_ELENUMBER_P");
				pe.setEleNumber(eleNumber);
			} else if (("F").equals(pe.getEleCategory())) {
				// 测试
				parseService.genTestDisplayViewByFunction(pe);
				String eleNumber = this.yqSequenceService.nextTextSequenceNumber("F", null, 3, "ELEMENT_ELENUMBER_F");
				pe.setEleNumber(eleNumber);
			} else if (("U").equals(pe.getEleCategory())) {
				// 测试
				String eleNumber = this.yqSequenceService.nextTextSequenceNumber("U", null, 3, "ELEMENT_ELENUMBER_U");
				pe.setEleNumber(eleNumber);
			}
		}
		if(("F").equals(pe.getEleCategory())){
			//测试
			parseService.genTestDisplayViewByFunction(pe);
		}

		pesave = this.save(pe);

		return this.translateParseElement(pesave);
	}

	@Override
	public List<ParseElementUse> findAllCategoryWithWFCategory() {
  		return this.initConstantsService.getParseElementUseList();
	}

 
	@Override
	public ParseElementDisplayView checkParseElementUse(ParseElement pe) {

		return parseService.genTestDisplayViewByFunction(pe);

	}

	@Override
	public ParseElement translateParseElement(ParseElement pe) {
		String translateExpress = parseService.translationChiness(pe, null);
		pe.setTranslateExpress(translateExpress);
		return pe;
	}

	@Override
	public ParseElement getParseElementByEleNumber(String eleNumber) {
		ParseElement pe = new ParseElement();
		String query = "SELECT spe.* FROM yq_parse_element spe WHERE spe.ele_number = '" + eleNumber + "'";
		List<ParseElement> pelist = this.sqlToModelService.executeNativeQuery(query, null, ParseElement.class);
		if (CommonUtil.isNotNull(pelist)) {
			pe = pelist.get(0);
		}
		return pe;
	}

	@Override
	public List<ParseElement> findParameterByElement(ParseElementQueryCriteria criteria) {
		List<ParseElement> pelist = new ArrayList<ParseElement>();
    	if (CommonUtil.isNotNull(criteria.getFunctionCode())) {
			ParseElement pe = this.getParseElementByEleNumber(criteria.getFunctionCode());
			if (CommonUtil.isNotNull(pe.getParameters())) {
				String parameters = "";
				String pp[] = pe.getParameters().split(",");
				for (int i = 0; i < pp.length; i++) {
					parameters = parameters + "'" + pp[i] + "',";
				}
				parameters = parameters.substring(0, parameters.length() - 1);
				String query = "SELECT spe.* FROM yq_parse_element spe WHERE spe.ele_number IN(" + parameters + ")";
				pelist = this.sqlToModelService.executeNativeQuery(query, null, ParseElement.class);
			}
		}
     return pelist;
	}
   public static void main(String[] args) {
	String arrayStr ="[{\"version\":0,\"createUser\":\"user04\",\"createTime\":\"2014/01/15 15:33:20\",\"lastUpdateUser\":\"user04\",\"lastUpdateTime\":\"2014/01/15 15:33:20\",\"pageMap\":{\"DATA_METHOD\":null,\"PROPERTY_NAME\":null,\"REFER_DETAIL\":null,\"DATA_SOURCE_EXPRESS\":null},\"id\":107,\"eleNumber\":\"P010\",\"eleName\":\"买满金额X\",\"description\":\"买满金额X\",\"eleCategory\":\"P\",\"useCategory\":\"ORD02\",\"usePropertyName\":null,\"system\":true,\"express\":null,\"dataType\":\"N\",\"scale\":null,\"deleted\":null,\"parameters\":null,\"parametersDsp\":null,\"eleCodeName\":\"P010-买满金额X\",\"checkedBy\":null,\"checkedDate\":null,\"valueSet\":null,\"segment1\":null,\"segment2\":null,\"segment3\":null,\"segment4\":null,\"segment5\":null,\"newValue\":null,\"translateExpress\":null,\"sourceStatusesList\":null,\"dataTypeDescription\":\"数据类型\",\"systemDescription\":\"是\",\"deletedDescription\":null,\"discountCategory\":null},{\"version\":0,\"createUser\":\"user04\",\"createTime\":\"2014/01/15 15:33:20\",\"lastUpdateUser\":\"user04\",\"lastUpdateTime\":\"2014/01/15 15:33:20\",\"pageMap\":{\"DATA_METHOD\":null,\"PROPERTY_NAME\":null,\"REFER_DETAIL\":null,\"DATA_SOURCE_EXPRESS\":null},\"id\":109,\"eleNumber\":\"P012\",\"eleName\":\"购买产品包编号(产品包编号)\",\"description\":\"产品包编号\",\"eleCategory\":\"P\",\"useCategory\":\"ORD02\",\"usePropertyName\":null,\"system\":true,\"express\":\"RA00012\",\"dataType\":\"C\",\"scale\":null,\"deleted\":null,\"parameters\":null,\"parametersDsp\":null,\"eleCodeName\":\"P012-购买产品包编号(产品包编号)\",\"checkedBy\":null,\"checkedDate\":null,\"valueSet\":null,\"segment1\":null,\"segment2\":null,\"segment3\":null,\"segment4\":null,\"segment5\":null,\"newValue\":null,\"translateExpress\":null,\"sourceStatusesList\":null,\"dataTypeDescription\":\"字符类型\",\"systemDescription\":\"是\",\"deletedDescription\":null,\"discountCategory\":null},{\"version\":0,\"createUser\":\"user04\",\"createTime\":\"2014/01/15 15:33:20\",\"lastUpdateUser\":\"user04\",\"lastUpdateTime\":\"2014/01/15 15:33:20\",\"pageMap\":{\"DATA_METHOD\":null,\"PROPERTY_NAME\":null,\"REFER_DETAIL\":null,\"DATA_SOURCE_EXPRESS\":null},\"id\":110,\"eleNumber\":\"P013\",\"eleName\":\"赠送产品包数量Y\",\"description\":\"赠送产品包数量Y\",\"eleCategory\":\"P\",\"useCategory\":\"ORD02\",\"usePropertyName\":null,\"system\":true,\"express\":null,\"dataType\":\"N\",\"scale\":null,\"deleted\":null,\"parameters\":null,\"parametersDsp\":null,\"eleCodeName\":\"P013-赠送产品包数量Y\",\"checkedBy\":null,\"checkedDate\":null,\"valueSet\":null,\"segment1\":null,\"segment2\":null,\"segment3\":null,\"segment4\":null,\"segment5\":null,\"newValue\":null,\"translateExpress\":null,\"sourceStatusesList\":null,\"dataTypeDescription\":\"数据类型\",\"systemDescription\":\"是\",\"deletedDescription\":null,\"discountCategory\":null},{\"version\":0,\"createUser\":\"developer\",\"createTime\":\"2015/03/20 11:20:38\",\"lastUpdateUser\":\"developer\",\"lastUpdateTime\":\"2015/03/20 11:20:38\",\"pageMap\":{\"DATA_METHOD\":null,\"PROPERTY_NAME\":null,\"REFER_DETAIL\":null,\"DATA_SOURCE_EXPRESS\":null},\"id\":-1228,\"eleNumber\":\"P018\",\"eleName\":\"外卖产品购买金额 W\",\"description\":\"外卖产品购买金额 W\",\"eleCategory\":\"P\",\"useCategory\":\"ORD02\",\"usePropertyName\":null,\"system\":true,\"express\":null,\"dataType\":\"N\",\"scale\":null,\"deleted\":null,\"parameters\":null,\"parametersDsp\":null,\"eleCodeName\":\"P018-外卖产品购买金额 W\",\"checkedBy\":null,\"checkedDate\":null,\"valueSet\":null,\"segment1\":null,\"segment2\":null,\"segment3\":null,\"segment4\":null,\"segment5\":null,\"newValue\":null,\"translateExpress\":null,\"sourceStatusesList\":null,\"dataTypeDescription\":\"数据类型\",\"systemDescription\":\"是\",\"deletedDescription\":null,\"discountCategory\":null},{\"version\":0,\"createUser\":\"developer\",\"createTime\":\"2015/04/08 15:44:12\",\"lastUpdateUser\":\"developer\",\"lastUpdateTime\":\"2015/04/08 15:44:12\",\"pageMap\":{\"DATA_METHOD\":null,\"PROPERTY_NAME\":null,\"REFER_DETAIL\":null,\"DATA_SOURCE_EXPRESS\":null},\"id\":-3222,\"eleNumber\":\"P019\",\"eleName\":\"是否包括物料产品\",\"description\":\"是否包括物料产品\",\"eleCategory\":\"P\",\"useCategory\":\"ORD02\",\"usePropertyName\":null,\"system\":true,\"express\":null,\"dataType\":\"B\",\"scale\":null,\"deleted\":null,\"parameters\":null,\"parametersDsp\":null,\"eleCodeName\":\"P019-是否包括物料产品\",\"checkedBy\":null,\"checkedDate\":null,\"valueSet\":null,\"segment1\":null,\"segment2\":null,\"segment3\":null,\"segment4\":null,\"segment5\":null,\"newValue\":null,\"translateExpress\":null,\"sourceStatusesList\":null,\"dataTypeDescription\":\"Boolean类型\",\"systemDescription\":\"是\",\"deletedDescription\":null,\"discountCategory\":null}]";
	List<ParseElement> list2 =jsonToList(arrayStr,ParseElement.class);
	 for(ParseElement p:list2) {
		 System.out.println(p.getCreateUser());
	 }
	 
  }
   
   public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
       List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
       return ts;
   }
   
   
//	@Override
//	public List<ChargeItem> findAllChargeItems(ParseElementQueryCriteria criteria) {
//		String sql = "select ci.* " + " from gc_charge_item ci WHERE ci.TIMES_CATEGORY in ('ONLY','MONTH','YEAR') ";
//		List<ChargeItem> parseElementList = sqlToModelService.executeNativeQuery(sql, null, ChargeItem.class);
//		if (parseElementList.isEmpty()) {
//			return new ArrayList<ChargeItem>();
//		}
//		return parseElementList;
//	}

//	@Override
//	public List<ChargeItem> findMonthChargeItems(ParseElementQueryCriteria criteria) {
//		String sql = "select ci.* " + " from gc_charge_item ci WHERE ci.TIMES_CATEGORY in ('MONTH','YEAR') ";
//	      if(CommonUtil.isNotNull(criteria.getKeyString())) {
//	    	  sql += " and concat(ci.CHARGE_CODE, ci.CHARGE_NAME, ifnull(ci.description,''))  LIKE '%"+criteria.getKeyString()+"%' ";
//	      }
//		List<ChargeItem> parseElementList = sqlToModelService.executeNativeQuery(sql, criteria, ChargeItem.class);
//		if (parseElementList.isEmpty()) {
//			return new ArrayList<ChargeItem>();
//		}
//		return parseElementList;
//	}
	
	/**
	 * 不包括渠道明细
	 * @param
	 * @return
	 */
//	@Override
//	public List<ChargeItem> findMonthChargeItemsExcChannelDtl(ParseElementQueryCriteria criteria) {
//		String sql = "select ci.* " + " from gc_charge_item ci WHERE ci.TIMES_CATEGORY in ('MONTH','YEAR') ";
//	      if(CommonUtil.isNotNull(criteria.getKeyString())) {
//	    	  sql += " and concat(ci.CHARGE_CODE, ci.CHARGE_NAME, ifnull(ci.description,''))  LIKE '%"+criteria.getKeyString()+"%' ";
//	      }
//	    sql = sql + " and ifnull(ci.parent_Charge_Code,'') not in (" + 
//	    		"				select p.CHARGE_CODE from gc_charge_item p where p.main_channel = 1)"  ;
//		List<ChargeItem> parseElementList = sqlToModelService.executeNativeQuery(sql, criteria, ChargeItem.class);
//		if (parseElementList.isEmpty()) {
//			return new ArrayList<ChargeItem>();
//		}
//		return parseElementList;
//	}

	@Override
	public List<ParseElement> findFunctionByChargeItem(String code) {
		String query = "SELECT pe.* FROM yq_parse_element pe WHERE 1=1 " + " AND pe.ELE_CATEGORY = 'F' "
				+ " AND pe.USE_PROPERTY_NAME ='" + code + "'";
		List<ParseElement> pelist = this.sqlToModelService.executeNativeQuery(query, null, ParseElement.class);
		if (pelist.isEmpty()) {
			return new ArrayList<ParseElement>();
		}
		return pelist;
	}

	@Override
	public List<ParseElement> findFunctionByUseCategory(String userCategory) {
		String query = "SELECT pe.* FROM yq_parse_element pe WHERE 1=1 " + " AND pe.ELE_CATEGORY = 'F' "
				+ " AND pe.USE_CATEGORY ='" + userCategory + "'";
		List<ParseElement> pelist = this.sqlToModelService.executeNativeQuery(query, null, ParseElement.class);
		if (pelist.isEmpty()) {
			return new ArrayList<ParseElement>();
		}
		return pelist;
	}

	@Override
	public ParseElementTerms saveParseElementTerms(ParseElementTerms parseElementTerms) {
		String funCode = parseElementTerms.getFunCode();
		if (!ParseElementTerms.CHARGE_TEXT.equals(funCode)) {
			ParseElement parseElement = this.parseService.getEleByCode(parseElementTerms.getFunCode(), null);
			String terms = parseElement.getDescription();
			String express = parseElementTerms.getFunCode() + "(";
			// F438(P253=0.008) F531(P301=0.6,P299=0.0400,P302=0.85,P243=0.0500)
			boolean isFristParam = true;
			for (ContractParamsView view : parseElementTerms.getParmList()) {
				terms = terms.replace("{" + view.getParamCode() + "}",
						"<b style=color:RED>" + view.getParamValue() + "</b>");
				if (isFristParam) {
					express = express + view.getParamCode() + "=" + view.getParamValue();
					isFristParam = false;
				} else {
					express = express + "," + view.getParamCode() + "=" + view.getParamValue();
				}
			}
			// 保存原始富文本
			parseElementTerms.setTerms(terms);
			parseElementTerms.setExpress(express + ")");
		}
		parseElementTerms = this.save(parseElementTerms);
		return parseElementTerms;
	}

	public ParseElementTerms getParseElementTerms(ParseElementTerms parseElementTerms, String type) {

		// 取最新的富文本
//		if ("edit".equals(type) && CommonUtil.isNotNull(parseElementTerms.getFunCode())) {
//			String editHtml = "";
//			if (ParseElementTerms.CHARGE_TEXT.equals(parseElementTerms.getFunCode())) {
//				editHtml = "<textarea data-role=\"editor\" maxlength=\"10000\" class=\"h5-text-input\" name=\"terms\""
//						+ "  style=\"width:95%;height:200px\"> " + parseElementTerms.getTerms() + "</textarea>";
//
//				parseElementTerms.setTerms(editHtml);
//			} else {
//
//				ParseElement parseElement = this.parseService.getEleByCode(parseElementTerms.getFunCode(), null);
//				if (parseElement != null) {
//					ContractFunctionView functionView = new ContractFunctionView();
//					functionView.setFunctionCode(parseElement.getEleNumber());
//					functionView.setFunctionName(parseElement.getEleName());
//					functionView.setDescription(parseElement.getDescription());
//					functionView.setParameters(parseElement.getParameters());
//					List<ContractParamsView> paramsList = new ArrayList<ContractParamsView>();
//					if (CommonUtil.isNotNull(parseElement.getParameters())) {
//						String[] params = parseElement.getParameters().split(",");
//						// P001,P002
//						for (String str : params) {
//							ParseElement pe = this.parseService.getEleByCode(str, null);
//							ContractParamsView conParam = new ContractParamsView();
//							this.setEleDateToParamView(pe, conParam);
//							if (pe.getDataType().equals(ParseConstants.DATA_TYPE_ENUM)) {
//								//conParam.setEnumList(this.getEnumListBySql(pe.getValueSet()));
//							}
//							paramsList.add(conParam);
//						}
//					}
//					functionView.setParamList(paramsList);
//
//					editHtml = editHtml + getEditHtml(functionView, parseElementTerms.getChargeCode(),
//							parseElementTerms.getExpress());
//					parseElementTerms.setTerms(editHtml);
//				}
//			}
//		}
		return parseElementTerms;
	}

	public ContractParamsView setEleDateToParamView(ParseElement ele,ContractParamsView param){
//		param.setDefaultValue(ele.getExpress());
//		param.setParamCode(ele.getEleNumber());
//		param.setParamName(ele.getEleName());
//		param.setParamType(ele.getDataType());
//		param.setValueSet(ele.getValueSet());
//		param.setScale(ele.getScale());
		return param;
	}
	
	public Map<String, String> genConFunctionCodeMap(String funParmValue) {

		Map<String, String> map = new HashMap<String, String>();
		if (CommonUtil.isNull(funParmValue)) {
			return map;
		}
		int paramStart = funParmValue.indexOf("(");
		int paramEnd = funParmValue.indexOf(")");
		if (paramStart > 0) {
			String paramsStr = funParmValue.substring(paramStart + 1, paramEnd);
			String[] parArray = paramsStr.split(",");
			for (String param : parArray) {
				String[] paramAndValue = param.split("=");
				String key = paramAndValue[0];
				String value = "";
				if (paramAndValue.length > 1) {
					value = paramAndValue[1];
				}
				map.put(key, value);
			}
		}
		return map;
	}

	private String getEditHtml(ContractFunctionView functionView, String chargeCode, String funParmValue) {
		String editHtml = functionView.getDescription();
		Map<String, String> funParmMap = genConFunctionCodeMap(funParmValue);
		for (ContractParamsView param : functionView.getParamList()) {
			if (ParseConstants.DATA_TYPE_ENUM.equals(param.getParamType())) {
				String html = "<div data-name='param' data-type='E' param-value='" + param.getParamValue()
						+ "' param-code='" + param.getParamCode() + "'  param-func='" + functionView.getFunctionCode()
						+ "' >";
				for (SelectItem selectItem : param.getEnumList()) {
					html = html + "<input type='checkbox' enum-key='" + selectItem.getItemKey() + "' data-name='enum'/>"
							+ selectItem.getItemName();
				}
				html += "</div>";
				editHtml = editHtml.replace("{" + param.getParamCode() + "}", html);
			} else {
				String value = "";
				if (funParmMap.containsKey(param.getParamCode())) {
					value = funParmMap.get(param.getParamCode());
				} else

				{
					if (CommonUtil.isNotNull(param.getParamValue())) {
						value = param.getParamValue();
					} else {
						value = param.getDefaultValue();
					}
				}
				String html = "<input data-type='" + param.getParamType() + "' data-name='param' param-func='"
						+ functionView.getFunctionCode() + "' param-code='" + param.getParamCode() + "' param-scale='"
						+ param.getScale() + "' data-set=\"" + param.getValueSet() + "\" class='edit' value='" + value
						+ "' style='width:15%;'/>";
				editHtml = editHtml.replace("{" + param.getParamCode() + "}", html);
			}

		}
		if (editHtml.indexOf("{" + chargeCode + "}") > 0) {
			String value = "";
			if (funParmMap.containsKey(chargeCode)) {
				value = funParmMap.get(chargeCode);
			}
			String html = "<input data-type='N' data-name='param' param-func='" + functionView.getFunctionCode()
					+ "' param-code='" + chargeCode + "' param-scale='2' data-set=\"\" class='edit' value='" + value
					+ "' style='width:15%;'/>";
			editHtml = editHtml.replace("{" + chargeCode + "}", html);
		}
		return editHtml;
	}

	@Override
	public ParseElementTerms getParseElementTerms(Long id) {

		return this.getById(ParseElementTerms.class, id);
	}

	@Override
	public List<ParseElementTerms> getParseElementTerms(String modelProperty, Long modelId, String chargeCode) {
		String query = "SELECT pet.* FROM YQ_PARSE_ELEMENT_TERMS pet" + " WHERE pet.model_property = '" + modelProperty
				+ "'" + " AND pet.model_id  = '" + modelId + "'";
		List<ParseElementTerms> pelist = this.sqlToModelService.executeNativeQuery(query, null,
				ParseElementTerms.class);
		return pelist;
	}

	private ContractFunctionView terms2FunctionView(ParseElementTerms parseElementTerms) {
		ContractFunctionView view = new ContractFunctionView();
		view.setFunctionCode(parseElementTerms.getFunCode());
		view.setFunctionName(parseElementTerms.getFunName());
		view.setDescription(parseElementTerms.getTerms());
		view.setFunctionId(parseElementTerms.getId());
		view.setTermsTitle(parseElementTerms.getTermsTitle());
		return view;
	}

	@Override
	public ContractFunctionView getParseElementTerms2FunctionView(Long id) {
		ParseElementTerms parseElementTerms = getParseElementTerms(id);
		return terms2FunctionView(parseElementTerms);
	}

	@Override
	public List<ContractFunctionView> getParseElementTerms2FunctionView(String modelProperty, Long modelId,
			String chargeCode) {
		List<ContractFunctionView> fviewList = new ArrayList<ContractFunctionView>();
		for (ParseElementTerms parseElementTerms : this.getParseElementTerms(modelProperty, modelId, chargeCode)) {
			fviewList.add(terms2FunctionView(parseElementTerms));
		}
		return fviewList;
	}
	
	/**
	 * 可以带参数也可以
	 * @param funCode
	 * @return
	 */
	@Override
	public List<SelectItem> getFunParaList(String funCode) {
		String query =" select p.ELE_NUMBER itemKey, p.ELE_NAME itemName "
				+" from yq_parse_element p where FIND_IN_SET(p.ELE_NUMBER,"
				+"		(select PARAMETERS from yq_parse_element f  where f.ELE_CATEGORY = 'F' and   INSTR('"+funCode+"',ELE_NUMBER) > 0  )) > 0 ";
		List<SelectItem> list = this.sqlToModelService
				.executeNativeQuery(query, null, SelectItem.class);
		return list;
	}

	@Override
	public List<ParseElement> findParameterByUseCategory(String userCategory) {
		String query = "SELECT pe.* FROM yq_parse_element pe WHERE 1=1 " + " AND pe.ELE_CATEGORY = 'P' "
				+ " AND pe.USE_CATEGORY ='" + userCategory + "'";
		List<ParseElement> pelist = this.sqlToModelService.executeNativeQuery(query, null, ParseElement.class);
		if (pelist.isEmpty()) {
			return new ArrayList<ParseElement>();
		}
		return pelist;
	}


	private List<SelectItem> findelementUseProperty(String useCategory) {
		ParseElementUse pu = this.initConstantsService.getParseElementUse(useCategory) ;
		return pu.getProperties();
	}

	@Override
	public List<SelectItem> findelementGetResult(String eleNumber) {

		List<SelectItem> sitemlist=new ArrayList<SelectItem>();
		SelectItem selectItem=new SelectItem();
		selectItem.setItemKey("true");
		selectItem.setItemName("是");
		sitemlist.add(selectItem);

		SelectItem selectItem2=new SelectItem();
		selectItem2.setItemKey("false");
		selectItem2.setItemName("否");
		sitemlist.add(selectItem2);

		return sitemlist;
	}

	@Override
	public List<SelectItem> findfunctionByParameter(String useCategory) {
		String query = "SELECT spe.ELE_NUMBER itemKey,concat(spe.ELE_NAME,'-',spe.ELE_NUMBER) itemName FROM yq_parse_element spe WHERE spe.ELE_CATEGORY='P' AND spe.USE_CATEGORY='"+useCategory+"'";
		return this.sqlToModelService.executeNativeQuery(query, null, SelectItem.class);
	}

}
