package com.yq.xcode.common.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import com.yq.xcode.common.bean.ContractFunctionView;
import com.yq.xcode.common.bean.ContractParamsView;
import com.yq.xcode.common.bean.ParseBaseElement;
import com.yq.xcode.common.bean.ParseElementDisplayView;
import com.yq.xcode.common.bean.ParseElementUse;
import com.yq.xcode.common.bean.ParseFunction;
import com.yq.xcode.common.bean.ParseParameter;
import com.yq.xcode.common.bean.ParseRootAndVariable;
import com.yq.xcode.common.bean.ParseStrModel;
import com.yq.xcode.common.bean.ParseUnit;
import com.yq.xcode.common.exception.ParseExprException;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.ParseElementDisplay;
import com.yq.xcode.common.model.ParseElementDisplayFun;
import com.yq.xcode.common.service.ParseElementService;
import com.yq.xcode.common.service.ParseService;
import com.yq.xcode.common.service.SqlToModelService;
import com.yq.xcode.common.service.YqSequenceService;
import com.yq.xcode.common.utils.CommonUtil;
import com.yq.xcode.common.utils.DateUtil;
import com.yq.xcode.common.utils.JPAUtils;
import com.yq.xcode.common.utils.JsUtil;
import com.yq.xcode.common.utils.ParseUtil;
import com.yq.xcode.constants.YqConstants;
import com.yq.xcode.constants.YqSelectHardcodeConstants;


@Service("ParseService")
public class ParseServiceImpl extends YqJpaDataAccessObject implements
		ParseService {
	
	private static Log LOG = LogFactory.getLog(ParseServiceImpl.class);
	@Autowired
	private SqlToModelService sqlToModelService;
//	@Autowired
//	private MonthSummaryService monthSummaryService;
//	
//	@Autowired
//	private ReceivableService receivableService;
	@Autowired
	private YqSequenceService yqSequenceService;
	
	@Autowired
	private ParseElementService parseElementService; 

	
	@Override
	public Object executeElementExpression(Object bean, String express, 
			 Map<String, ParseElement> elements,Map<String, String> newParas) {		
		return this.parseAndExecuteExpression(bean, express, null, elements, newParas);
	}
	
	@Override
	public Boolean isTrueElementExpression(Object bean, String express, 
			 Map<String, ParseElement> elements,Map<String, String> newParas) {		
		return (Boolean)this.parseAndExecuteExpression(bean, express, null, elements, newParas);
	}
	
	
	/**
	 * 所有的参数都替换完成后，最后执行表达式, 可以报考 ';' 分隔的多表达式
	 * @param bean
	 * @param express
	 * @param simpleContext
	 * @return
	 */
	private Object executeExpression(Object bean, String express,StandardEvaluationContext simpleContext,Map<String, Object> variables) {
		if (CommonUtil.isNull(express)) {
    		return null;
    	}
    	if (simpleContext == null) {
    		simpleContext = this.genBaseSimpleContext(bean);
    	}
    	ExpressionParser parser= new SpelExpressionParser();  
    	if (!CommonUtil.isNull(variables)) {
    		simpleContext.setVariables(variables);
    	}
    	if (express.contains(";")) { // 执行的是多个事件， 返回的是空
    		String[] funArr = express.split(";");
			for (String fun : funArr) {
				if (CommonUtil.isNotNull(fun)) {
					this.executeExpressionOnly(bean, fun, simpleContext, parser); 
				}
			}
    		return null;
    	} else {
    		return this.executeExpressionOnly(bean, express, simpleContext, parser);
    	}
    	 
		
	}
	
	
	private Object executeExpressionOnly( Object bean, String express, StandardEvaluationContext simpleContext,ExpressionParser parser) {
		if (CommonUtil.isNull(express)) {
    		return null;
    	}
 
		try {
			System.out.println("$%^$%^$%^$%^$%^$%^ :   "+express);
		    Expression exp=parser.parseExpression(express); 
			Object obj = exp.getValue(simpleContext);
			return obj;
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exp --> " + express);
			if (CommonUtil.isNull(bean)) {
				throw new ValidateException("表达式错误,Bean is null express : " + express, e);
			}
			throw new ValidateException("表达式错误,Bean : "+bean.getClass()+" express : " + express, e);
		}
		
	}
	
    /**
     * 解析执行表达式
     */
	
    private Object parseAndExecuteExpression(Object bean, String express, StandardEvaluationContext simpleContext,Map<String, ParseElement> elements,Map<String, String> newParas) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = express;
    	if (CommonUtil.isNull(express)) {
    		return express;
    	}
    	Map<String, Object> tempVariables = new HashMap<String, Object>();
    	int cont = 1;
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			break;
    		} else {
    			String currStr =  psm.getCurrStr().trim();
    			Object[] oA = this.parseEleCodeWithPars(currStr);
    			String eleNumber = (String)oA[0];
    			Map<String,String> eleParas = (Map<String,String>)oA[1];
    			Object value = null;
    			ParseElement pv = elements.get(eleNumber);    			
    			if (pv == null) {
    				throw new ValidateException("在表达式：（"+express+"）中，元素编码【"+eleNumber+"】不存在");
    			} else {
    				String tmStr = "";
    				if (eleParas != null) {
    					if (newParas == null) {
    						newParas = new HashMap<String,String> ();
    					} 
    					newParas.putAll(eleParas);
    				}
    				value = this.genValueByEleNumber(bean, eleNumber,   elements, newParas);    				
    				if (YqSelectHardcodeConstants.PropertyType.NUMBER.equals(pv.getDataType()) || YqSelectHardcodeConstants.PropertyType.INTEGER.equals(pv.getDataType())) {
    					tmStr = CommonUtil.nvl(value, "0").toString();
    				} else if (YqSelectHardcodeConstants.PropertyType. BOOLEAN.equals(pv.getDataType())) {
    					String key = eleNumber;
    					cont++;
    					tmStr = "#"+key;
    					tempVariables.put(key, CommonUtil.nvl(value, false));
    				} else {
    					String key = eleNumber;
    					cont++;
    					tmStr = "#"+key;
    					tempVariables.put(key, value);
    				}
    				psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
    			}    			
    		}
    	}
    	return this.executeExpression(bean, psmStr, simpleContext, tempVariables);
    }

	private StandardEvaluationContext genBaseSimpleContext(Object rootObject) {
		StandardEvaluationContext simpleContext = new StandardEvaluationContext();
 
		if (rootObject != null) {
			if (rootObject instanceof ParseRootAndVariable) {
				ParseRootAndVariable rv = (ParseRootAndVariable)rootObject;
				if (rv.getRootObject() != null) {
					simpleContext.setRootObject(rv.getRootObject());
				}
				if (rv.getVariable() != null) {
					simpleContext.setVariables(rv.getVariable());
				}
			} else if (rootObject instanceof Map){
				simpleContext.setVariables((Map)rootObject);
			} else {
				simpleContext.setRootObject(rootObject);
			}
		}
		simpleContext.setVariable("pt", new ParseUtil()); // 通用方法
		return simpleContext;
	}
	@Override
	public Map<String, ParseElement> getElementsByUseCategory(String usecategory) {
		List<ParseElement> list = this.find(" from ParseElement where useCategory = '"+usecategory+"'");
		return CommonUtil.ListToMap(list, "eleNumber");
	}
	
	@Override
	public List<ParseElement> getElementslistByUseCategory(String usecategory) {
		List<ParseElement> list = this.find(" from ParseElement where useCategory = '"+usecategory+"' order by eleName");
		return list;
	}
	
	@Override
	public Map<String, ParseElement> getElementsByFunctionCode(String functionCode) {
		ParseElement pe = this.getEleByCode(functionCode,null);
		if (pe == null) {
			return null;
		}
		List<ParseElement> list = this.find(" from ParseElement where useCategory = '"+pe.getUseCategory()+"'");
		return CommonUtil.ListToMap(list, "eleNumber");
	}
	
 
	

	
	private Object genValueByParseElement(Object bean, ParseElement element,
			 Map<String, ParseElement> elements, Map<String, String> newParas) {
		StandardEvaluationContext myContext = null;
		ParseElement parseEle = element;
		Map<String, ParseElement> eleMap = elements;
		if (eleMap == null) {
			eleMap = this.getElementsByUseCategory(parseEle.getUseCategory());
		}
		if (myContext == null) {
			myContext = this.genBaseSimpleContext(bean);
		}
		if (YqSelectHardcodeConstants.ParseElement_eleCategory.PARAMETER.equals(parseEle.getEleCategory())) { //参数
			ParseParameter para = parseEle;
			String pValue = para.getExpress();
			if (newParas != null && newParas.keySet().contains(parseEle.getEleNumber())) {
				pValue = newParas.get(parseEle.getEleNumber());
			}
			LOG.info("参数:"+para.getEleNumber()+"  ="+this.toParseObject(pValue, para));
			return this.toParseObject(pValue, para);			
		} else if (YqSelectHardcodeConstants.ParseElement_eleCategory.UNIT.equals(parseEle.getEleCategory())){ //因子
			ParseUnit unit = parseEle;
			//System.out.println("fuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuck " + element.getEleName());
			//Object value = this.executeExpression(bean, unit.getExpress(), null,null);	//支持因子待函数
			Object value = this.parseAndExecuteExpression(bean, unit.getExpress(), null, eleMap, newParas);
			LOG.info("因子:"+unit.getEleNumber() +"="+value+"   计算公式="+unit.getExpress());
			return this.toParseObject(value, unit);
		} else { //函数
			ParseFunction function = parseEle;
			Object fValue = this.parseAndExecuteExpression(bean, function.getExpress(), null, eleMap, newParas);
//            if (ParseConstants.DATA_TYPE_NUMBER_EXPR.equals(function.getDataType())) {
//            	fValue = this.parseAndExecuteExpression(bean, (String)fValue, null, eleMap, newParas);
//            }
 			//LOG.info("函数--"+function.getEleNumber()+" ("+function.getDescription()+") "+  " 结果="+fValue);
			//LOG.info("函数--"+function.getEleNumber()+" |公式 ="+ function.getExpress()+     " 结果="+fValue);
			return this.toParseObject(fValue, function);
		}
	}
	@Override
	public Object genValueByEleNumber(Object bean, String eleNumber,
			 Map<String, ParseElement> elements, Map<String, String> newParas) {
		if (CommonUtil.isNull(eleNumber)) {
			throw new ValidateException("元素编码不可为空");
		}
		ParseElement parseEle = null;
		if (elements != null) {
			parseEle = elements.get(eleNumber);
		}
		if (parseEle == null) {
			parseEle = this.getEleByCode(eleNumber,elements);
			if (elements != null) {
				elements.put(parseEle.getEleNumber(), parseEle);
			}
		}		
		if (parseEle == null) {
			throw new ValidateException("元素编码【"+eleNumber+"】不存在");
		} 
		return this.genValueByParseElement(bean, parseEle, elements, newParas);
	}
	
	private ParseElement getParseElement(String eleNumber,
			 Map<String, ParseElement> elements ) {
		if (CommonUtil.isNull(eleNumber)) {
			throw new ValidateException("元素编码不可为空");
		}
		ParseElement parseEle = null;
		if (elements != null) {
			parseEle = elements.get(eleNumber);
		}
		if (parseEle == null) {
			parseEle = this.getEleByCode(eleNumber,elements);
		}		
		if (parseEle == null) {
			throw new ValidateException("元素编码【"+eleNumber+"】不存在");
		}
		return parseEle;
	}
	
	public ParseElementDisplayView genTestDisplayViewByFunction(ParseElement function) {
		try {
			List<ParseElementDisplayView> funList = new ArrayList<ParseElementDisplayView>();
			ParseElementUse ue = this.getElementUserByCategory(function.getUseCategory());
			Object bean = ue.getTestEntity();
			if (CommonUtil.isNull(bean)){
				return new ParseElementDisplayView();
			}
			return this.genDisplayViewByFunction(bean, function, null,null,funList,null);
		} catch (Exception e) {
			throw new ParseExprException(e);
		}		
	}
	
	public ParseElement getEleByCode(String eleNumber,Map<String,ParseElement> eMap) {
		if (eMap != null) {
			ParseElement pe = eMap.get(eleNumber);
			 if (pe != null) {
				 return pe;
			 }
		} 
		return (ParseElement)this.getSingleRecord(" from ParseElement where eleNumber = '"+eleNumber+"'");
	}
	private Object toParseObject(Object value, ParseBaseElement baseEle) {
		if (value instanceof String ) {
			String sv = (String)value;
			if (YqSelectHardcodeConstants.PropertyType.DATE.equals(baseEle.getDataType())) {
				return DateUtil.convertString2Date(sv, DateUtil.DEFAULT_DATE_FORMAT);
			} else if (YqSelectHardcodeConstants.PropertyType.NUMBER.equals(baseEle.getDataType()) ) {
				return new BigDecimal(sv);
			} else if (YqSelectHardcodeConstants.PropertyType. BOOLEAN.equals(baseEle.getDataType())) {
				if (CommonUtil.isNull(sv)) {
					return false;
				} else {
					return "Y".equals(sv.toUpperCase());
				}
			} else {
				return sv;
			}
		} else {
			if (YqSelectHardcodeConstants.PropertyType. NUMBER.equals(baseEle.getDataType()) ) {
				if (CommonUtil.isNull(value)) {
					return new BigDecimal("0");
				} else {
					int scale = 2;
					if (baseEle.getScale()!= null) {
						scale = baseEle.getScale();
					}
					return JsUtil.round(new BigDecimal(value.toString()),scale);
				}
			} 
			return value;
		}

	}
	@Override
	public ParseElementDisplayView genDisplayViewByEleCode(Object bean, String eleCode, 
			 Map<String, ParseElement> elements, Map<String, String> newParas ) {
		return this.genDisplayViewByEleCode(bean,eleCode,elements,newParas,null);
	}

	@Override
	public ParseElementDisplayView genDisplayViewByEleCode(Object bean, String eleCode, 
			 Map<String, ParseElement> elements, Map<String, String> newParas,String unitParStr) {
		ParseElement pe = null;
		if (elements == null) {
			pe = this.getEleByCode(eleCode,elements);
		} else {
			pe = elements.get(eleCode);
		}
		if (pe == null) {
			throw new ValidateException("元素编码：【"+eleCode+"】不存在");
		}
		List<ParseElementDisplayView> funList = new ArrayList<ParseElementDisplayView>();
		ParseElementDisplayView pView = this.genDisplayViewByFunction(bean, pe, newParas,elements,funList,unitParStr);
		pView.setFunList(funList);
		return pView;
	}
	
	private ParseElementDisplayView genDisplayViewByFunction(Object bean, ParseElement function, 
			Map<String, String> newParas, Map<String, ParseElement> elements,List<ParseElementDisplayView> funList,String unitParStr ) {
		ParseElementDisplayView peView = new ParseElementDisplayView();
		Map<String,ParseElement> eMap = elements;
		if (elements == null) {
			eMap = this.getElementsByUseCategory(function.getUseCategory());
		}
		peView.setParseElement(function);
		
//        if (ParseConstants.DATA_TYPE_NUMBER_EXPR.equals(function.getDataType())) {
//        	Object fValue = this.parseAndExecuteExpression(bean, function.getExpress(), null, eMap, newParas);
//        	peView.setDisplayExpress(this.translationChinessWithValue(bean, (String)fValue,eMap, newParas,funList,unitParStr));
//        } else {
        	peView.setDisplayExpress(this.translationChinessWithValue(bean, function.getExpress(),eMap, newParas,funList,unitParStr));	
 //       }		
		peView.setValue(this.genValueByParseElement(bean, function, eMap, newParas));
		return peView;
	}
	
	private String translationChinessWithValue(	Object bean,
		String express,Map<String,ParseElement> eMap, Map<String, String> newParas,List<ParseElementDisplayView> funList,String unitParStr){
		if (CommonUtil.isNull(express) || !express.contains("{")) {
			return express;
		}
		String psmStr = express;
		String startCh = "{";
		String endCh = "}";
		String newStr = "";
		while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, "{", "}");
    		if (!psm.isSplitted()) {
    			newStr = newStr + psmStr;
    			break;
    		} else {
    			String currStr = psm.getCurrStr().trim(); 
    			Object[] oA = parseEleCodeWithPars(currStr);
    			String eleNumber = (String)oA[0];
    			ParseElement baseEle = eMap.get(eleNumber);  			
    			if (baseEle == null) {
    				throw new ValidateException("在表达式：（"+express+"）中，元素编码【"+eleNumber+"】不存在");
    			}  else {
    				//newStr = newStr+psm.getPreStr()+ startCh+pv.getEleName()+endCh;
    				if (oA[1] != null) {
    					if (newParas == null) {
    						newParas = new HashMap<String,String>();
    					}
    					Map<String,String> parMap = (Map<String,String>) oA[1];
    					newParas.putAll(parMap);
    					for (String pCode : parMap.keySet()) {
    						ParseElement pv = eMap.get(pCode);  
    						if (pv == null) {
    		    				throw new ValidateException("在表达式：（"+express+"）中，元素编码【"+pCode+"】不存在");
    		    			} else {
    		    				currStr = currStr.replace(pCode, "<strong>"+eMap.get(pCode).getEleName()+"</strong>");
    		    			}
    					}
    				}
    				currStr =  currStr.replace(eleNumber, "<strong>"+baseEle.getEleName()+"</strong>"); //pv.getEleName() ;
    				
    				Object value = this.genValueByEleNumber(bean, eleNumber,  eMap,  newParas);
    				if ("F".equals(baseEle.getEleCategory())) { //是函数
        				//newStr = newStr+psm.getPreStr()+ startCh+currStr+":"+value+endCh;
    					newStr = newStr+psm.getPreStr() //+ startCh
    							+"<a class=\"k-myclick\" style=\"min-width: 0px;\"  href=\"javascript:;\" " 
			                    + " data-clickname=\"opendtl\" data-pDisplayId=\""
    							+YqConstants.Placeholder.pDisplayId+"\" data-name=\""+baseEle.getEleNumber()+"\" >"
    							+ currStr
      							+"</a>"
      							+":"+this.formatValue(baseEle, value)+" "+CommonUtil.nvl(baseEle.getUnitName(), "");
    							//+endCh;
        				ParseElementDisplayView pView = this.genDisplayViewByFunction( bean, baseEle, 
        						 newParas,  eMap, funList,unitParStr);
        				funList.add(pView);
    				} else if ("U".equals(baseEle.getEleCategory()) && CommonUtil.isNotNull(unitParStr)
    						&& CommonUtil.isNotNull(baseEle.getProviderName())
    						) { //是元素
        				//newStr = newStr+psm.getPreStr()+ startCh+currStr+":"+value+endCh;
    					
    					newStr = newStr+psm.getPreStr() //+ startCh
    							+"<a class=\"k-myclick\" style=\"min-width: 0px;\"  href=\"javascript:;\" " 
			                    + " data-clickname=\"openunitdtl\" "
			                    + " data-unitParStr=\""+unitParStr+"\""
			                    + "\" data-name=\""+baseEle.getEleNumber()+"\" >"
    							+ currStr
    							+"</a>"
    							+":"+this.formatValue(baseEle, value)+" "+CommonUtil.nvl(baseEle.getUnitName(), "");
    							//+endCh;
        				ParseElementDisplayView pView = this.genDisplayViewByFunction( bean, baseEle, 
        						 newParas,  eMap, funList,unitParStr);
        				funList.add(pView);
    				} else {
       				   // newStr = newStr+psm.getPreStr()+ startCh+currStr+":"+value+endCh;
    					 newStr = newStr+psm.getPreStr()+  currStr+":"+this.formatValue(baseEle, value) +" "+CommonUtil.nvl(baseEle.getUnitName(), "");
    				}
    				psmStr = psm.getPostStr();
    			}
    		}
    	}
		return newStr;
			

	}
	
	private String formatValue(ParseElement pe, Object value) { 
		if (CommonUtil.isNull(value)) {
			return "";
		}
//		if (pe.getDataType().equals(YqConstants.PropertyType.DATA_TYPE_PNUMBER)) {
//			BigDecimal pnValue = new BigDecimal( CommonUtil.nvl(value, "0").toString());
//			BigDecimal formatValue = JsUtil.calculate(pnValue, "*", new BigDecimal("100"));
//			return  formatValue + "%"  ;
//		}else {
			return value.toString()  ;
//		} 
	}


 

	@Override
	public String translationChiness(ParseFunction function,Map<String,ParseElement> eMap) {

		String express = function.getExpress();
		if (CommonUtil.isNull(express) || !express.contains("{")) {
			return express;
		}
		if (eMap == null) {
			eMap = this.getElementsByUseCategory(function.getUseCategory());
		}
		
		String psmStr = express;
		String startCh = "{";
		String endCh = "}";
		String newStr = "";
		while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, "{", "}");
    		if (!psm.isSplitted()) {
    			newStr = newStr + psmStr;
    			break;
    		} else {
    			String currStr = psm.getCurrStr().trim(); 
    			Object[] oA = parseEleCodeWithPars(currStr);
    			String eleNumber = (String)oA[0];
    			ParseElement baseEle = eMap.get(eleNumber);  			
    			if (baseEle == null) {
    				throw new ValidateException("在表达式：（"+express+"）中，元素编码【"+eleNumber+"】不存在");
    			}  else {
    				//newStr = newStr+psm.getPreStr()+ startCh+pv.getEleName()+endCh;
    				if (oA[1] != null) {
    					Map<String,String> parMap = (Map<String,String>) oA[1];
    					for (String pCode : parMap.keySet()) {
    						ParseElement pv = eMap.get(pCode);  
    						if (pv == null) {
    		    				throw new ValidateException("在表达式：（"+express+"）中，元素编码【"+pCode+"】不存在");
    		    			} else {
    		    				currStr = currStr.replace(pCode, eMap.get(pCode).getEleName());
    		    			}
    					}
    				}
    				newStr = newStr+psm.getPreStr()+ currStr.replace(eleNumber, baseEle.getEleName()); //pv.getEleName() ;
    				psmStr = psm.getPostStr();
    			}
    			
    		}
    	}
		return newStr;
	}


	@Override
	public Boolean isTrueByEleNumber(Object bean, String eleCode,
			Map<String, ParseElement> element, Map<String, String> newParas) {		
		return (Boolean)this.genValueByEleNumber(bean, eleCode, element, newParas );
	}

	@Override
	public Object genValueByEleNumberWithPars(Object bean,
			String eleCodeWithPars, Map<String, ParseElement> element) {
		Object[] oA = this.parseEleCodeWithPars(eleCodeWithPars);
		return this.genValueByEleNumber(bean, (String)oA[0], element,(Map<String,String>)oA[1]);
	}



	@Override
	public Boolean isTrueByEleNumberWithPars(Object bean,
			String eleCodeWithPars, Map<String, ParseElement> elements) {
		Object[] oA = this.parseEleCodeWithPars(eleCodeWithPars);
		Boolean b = this.isTrueByEleNumber(bean, (String)oA[0], elements,(Map<String,String>)oA[1]);
		return b;
	}


//	/**
//	 * 格式 eleCode(P001=11,P002=12)
//	 * Object[0] = eleCode
//	 * Object[1] = Map<String,String> 参数
//	 * @param eleCodeWithPars
//	 * @return
//	 */
//	private Object[] parseEleCodeWithPars(String eleCodeWithPars) {
//		Object[] oA = new Object[2];
//		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(eleCodeWithPars, "(", ")");
//		if (psm.isSplitted()) {
//			String pars = psm.getCurrStr().trim();
//			if (CommonUtil.isNotNull(pars)) {
//				Map<String,String> parMap = new HashMap<String,String>();
//				String[] sA = pars.split(",");
//				for (String s : sA) {
//					String[] pValue = s.split("=");
//					if (pValue.length > 2 || pValue.length <1) {
//						throw new ValidateException("参数["+eleCodeWithPars+"]格式有误");
//					} else if (pValue.length == 2) {
//						parMap.put(pValue[0], pValue[1]);
//					} else {
//						parMap.put(pValue[0], null);
//					}
//				}
//				oA[1] = parMap;
//			}
//			oA[0] =psm.getPreStr();
//		} else {
//			oA[0] = eleCodeWithPars;
//		}
//		
//		return oA;
//	}

	@Override
	public List<ParseParameter> findAndResetFunctionParameters(
			String functionCodeWithPars, Map<String,ParseElement> eMap) {
		Object[] sA = this.parseEleCodeWithPars(functionCodeWithPars);
		String pCode = (String)sA[0];
		Map<String,String> newValueMap = (Map<String,String>)sA[1];
		ParseElement be = this.getEleByCode(pCode, eMap);
		if (be == null) {
			throw new ValidateException("元素代码"+pCode+"不存在");
		}
		List<ParseParameter> pList = this.find(" from ParseElement p where p.useCategory = ? and p.eleCategory = ? ",be.getUseCategory(),be.getEleCategory() );
		if (CommonUtil.isNotNull(newValueMap)) {
			for (ParseParameter p : pList) {
				String newValue = newValueMap.get(p.getEleNumber());
				if (CommonUtil.isNotNull(newValue)) {
					p.setNewValue(newValue);
				}
			}
		}		
		return pList;
	}

	@Override
	public String zipParametersNewValue(List<ParseParameter> parameters) {
		String zipStr = "";
		for (ParseParameter p : parameters) {
			if (CommonUtil.isNotNull(p.getNewValue())) {
				if (CommonUtil.isNull(zipStr)) {
					zipStr = p.getEleNumber()+"="+p.getNewValue();
				} else {
					zipStr = zipStr+","+p.getEleNumber()+"="+p.getNewValue();
				}
			}
		}
		if (CommonUtil.isNotNull(zipStr)) {
			return "("+zipStr+")";
		}		
		return null;
	}
	
	private ParseElementUse getElementUserByCategory(String userCategory) {
		List<ParseElementUse> ueList = this.parseElementService.findAllCategoryWithWFCategory();
		for (ParseElementUse ue : ueList ) {
			if (userCategory.equals(ue.getCode())) {
				return ue;
			}
		}
		throw new ValidateException(userCategory+" 用途代码不存在!");
	}

	@Override
	public ParseElementDisplayView genDisplayViewByEleCodeWithPars(Object bean,
			String eleCodeWithPars, Map<String, ParseElement> elements) {
		return this.genDisplayViewByEleCodeWithPars(bean, eleCodeWithPars, elements, null);
	}
	@Override
	public ParseElementDisplayView genDisplayViewByEleCodeWithPars(Object bean,
			String eleCodeWithPars, Map<String, ParseElement> elements,String unitParStr) {
		String[] funs = eleCodeWithPars.split(";");
		if (funs.length > 1) {
			ParseElementDisplayView peView = new ParseElementDisplayView();
			List<ParseElementDisplayView> funList = new ArrayList<ParseElementDisplayView>();
			ParseElement parseElement = new ParseElement();
			parseElement.setEleNumber(eleCodeWithPars);
			peView.setParseElement(parseElement);
  
			String expr = null;
			Object[] oA = this.parseEleCodeWithPars(funs[0]);
			String eleCode = (String)oA[0];
			for (String fun : funs) {
 				if (expr == null) {
					expr="{"+fun+"}";
				} else {
					expr=expr+"+\n{"+fun+"}";
				}
			} 
			System.out.println("eleCodeWithPars = "+eleCodeWithPars+"   eleCode ="+eleCode);
			Map<String,ParseElement> eMap = this.getElementsByUseCategory(getEleByCode(eleCode,elements).getUseCategory());
        	Object fValue = this.parseAndExecuteExpression(bean, expr, null, eMap, null);
        	peView.setDisplayExpress(this.translationChinessWithValue(bean, expr,eMap, null,funList,unitParStr));
        	peView.setValue(fValue);
        	peView.setFunList(funList);
        	return peView;

		} else {
			Object[] oA = this.parseEleCodeWithPars(eleCodeWithPars);
			String eleCode = (String)oA[0];
			Map<String,String> newParas = (Map<String,String>)oA[1];
			return this.genDisplayViewByEleCode(bean, eleCode, elements, newParas,unitParStr);
		}
		
	}
	
	private ParseElementDisplayView genDisplayViewByEleCodeWithParsTmp(Object bean,
			String eleCodeWithPars, Map<String, ParseElement> elements) {
		Object[] oA = this.parseEleCodeWithPars(eleCodeWithPars);
		String eleCode = (String)oA[0];
		Map<String,String> newParas = (Map<String,String>)oA[1];
		return this.genDisplayViewByEleCode(bean, eleCode, elements, newParas);
	}
	
	

	@Override
	public String translationEleCodeWithParsToChiness(String eleCodeWithParas,Map<String, ParseElement> eMap) {
		Object[] oA = this.parseEleCodeWithPars(eleCodeWithParas);
		String eleCode = (String)oA[0];
		if (eMap == null) {
			eMap= this.getElementsByFunctionCode(eleCode);
			if (eMap == null) {
				LOG.info(" eleCode : "+eleCode +" 不存在！");
				return "";
			//	return " eleCode : "+eleCode +" 不存在！";
			}
		}		
		String startCh = "{";
		String endCh = "}";
		ParseElement tmp = eMap.get(eleCode);
		if (tmp == null) {
			throw new ValidateException("元素编码【"+eleCode+"】不存在， 可能被删除， 数据问题， 请联系管理员!");
		}
		String psmStr = tmp.getEleName();
		Map<String,String> parMap = (Map<String,String>) oA[1];
		if (CommonUtil.isNotNull(parMap)) {
			psmStr=psmStr+"(";
			Iterator it = parMap.keySet().iterator();
			while (it.hasNext()) {
				String pCode = (String)it.next();
				String name = eMap.get(pCode).getEleName();
				String value = parMap.get(pCode);
				psmStr=psmStr+name+" = "+value+",";
			}
			psmStr = psmStr.substring(0, psmStr.length() - 1) + ")";
		}		
		return psmStr;
	}
	
	@Override
	public List<ContractFunctionView> genConFunctionCodeFormatList(String functionCode,Map<String, ParseElement> eMap) {
		List<ContractFunctionView> fviewList = new ArrayList<ContractFunctionView>(); 
		if (CommonUtil.isNotNull(functionCode) ) {
			 String[] funs = functionCode.split(";");
 			 int ind = 1;
			 for (String fun : funs) {
 				 ContractFunctionView funView = this.genConFunctionCodeFormat(fun, eMap);
 				 funView.setInd(ind);
				 fviewList.add(funView);
				 ind++;
			 } 
		 }
		 return fviewList;
	}

	@Override
	public ContractFunctionView genConFunctionCodeFormat(String functionCode,Map<String, ParseElement> eMap) {
		if (CommonUtil.isNull(functionCode)) {
			return null;
		}
		ContractFunctionView funView = new ContractFunctionView();
		funView.setFunctionCode(functionCode);
		StringBuffer paramsCn = new StringBuffer();
		List<ContractParamsView> paramList = new ArrayList<ContractParamsView>();
		String code;
		String paramsStr;
		int paramStart = functionCode.indexOf("(");
		int paramEnd = functionCode.indexOf(")");
		if (paramStart>0) {
			code=functionCode.substring(0,paramStart);
			paramsStr = functionCode.substring(paramStart+1, paramEnd);
			String[] parArray = paramsStr.split(",");
			String br = "";
			for (String param : parArray) {
				String[] paramAndValue = param.split("=");
				String key = paramAndValue[0];
				String value = "";
				if (paramAndValue.length > 1) {
					value = paramAndValue[1];
				}
				ParseElement pe = eMap.get(paramAndValue[0]);
//				if (pe.getDataType().equals(ParseConstants.DATA_TYPE_PNUMBER)) {
//					BigDecimal pnValue = new BigDecimal((String)CommonUtil.nvl(value, "0"));
//					BigDecimal formatValue = JsUtil.calculate(pnValue, "*", new BigDecimal("100"));
//					paramsCn.append(br+"<strong>"+pe.getEleName()+"</strong>=").append(formatValue.toString()+ "%"  );
//				}else {
					paramsCn.append(br+"<strong>"+pe.getEleName()+"</strong>=").append(value   );
//				}
				br = "</br>";
			}
			//System.out.println(functionCode+">>>>"+code);
			ParseElement pe = eMap.get(code);
			if (pe == null) {
				funView.setFunctionName( code);
			} else {
				funView.setFunctionName(pe.getEleName());
			}
 			funView.setParamsCn(paramsCn.toString()); 
		}else {
			code = functionCode;
			paramsStr = "";
			if (CommonUtil.isNotNull(code) && eMap.containsKey(code)) {
				funView.setFunctionName(eMap.get(code).getEleName());
			}
		}
		
		return funView;
		
		
//		Object[] oA = this.parseEleCodeWithPars(functionCode);
//		funView.setFunctionName(eMap.get(oA[0]).getEleName());
//		funView.setParamsCn(paramsCn.toString());
//		System.out.println("1");
//		return null;
	}
	
	/**
	 * 0 - 显示名称
	 * 1 - value
	 * 2 - code
	 */
	@Override
	public List<String[]> getParamsCn(String functionCode,Map<String, ParseElement> eMap){
		List<String[]> paramsCns = new ArrayList<String[]>();
		if (CommonUtil.isNull(functionCode)) {
			return paramsCns;
		}
		String paramsStr;
		int paramStart = functionCode.indexOf("(");
		int paramEnd = functionCode.indexOf(")");
		if (paramStart>0) {
			paramsStr = functionCode.substring(paramStart+1, paramEnd);
			String[] parArray = paramsStr.split(",");
			for (String param : parArray) {
				String[] paramAndValue = param.split("=");
				String value = "";
				if (paramAndValue.length > 1) {
					value = paramAndValue[1];
				}
				ParseElement pe = eMap.get(paramAndValue[0]);
//				if (pe.getDataType().equals(ParseConstants.DATA_TYPE_PNUMBER)) {
//					BigDecimal pnValue = new BigDecimal((String)CommonUtil.nvl(value, "0"));
//					BigDecimal formatValue = JsUtil.calculate(pnValue, "*", new BigDecimal("100"));
//					paramsCns.add(new String[]{pe.getEleName(),formatValue.toString()+ "%",pe.getEleNumber()});
//				}else {
					paramsCns.add(new String[]{pe.getEleName(),value,pe.getEleNumber()});
//				}
			}
		}
		return paramsCns;
	}

	@Override
	public ParseElementDisplay createParseElementDisplay(String modelProperty,
			Long modelId,Long modelDtlId,  ParseElementDisplayView pView) { 
		ParseElementDisplay pDisp = new ParseElementDisplay();
		Long id = this.genParseElementDisplayId();
		pDisp.setId(id);
		pDisp.setModelId(modelId);
		pDisp.setModelDtlId(modelDtlId);
		pDisp.setModelProperty(modelProperty);
		pDisp.setFunCode(pView.getParseElement().getEleNumber());
		pDisp.setValue(CommonUtil.nvl(pView.getValue(), "").toString());
		pDisp.setDisplayExpress(this.replacepDisplayId(pView.getDisplayExpress(),id));
		pDisp = this.create(pDisp);
		if (CommonUtil.isNotNull(pView.getFunList())) {
			List<ParseElementDisplayFun> funList = new ArrayList<ParseElementDisplayFun>();
			for (ParseElementDisplayView funView : pView.getFunList()) {
				ParseElementDisplayFun fun = new ParseElementDisplayFun();
				fun.setParseElementDisplayId(pDisp.getId());
				fun.setFunCode(funView.getParseElement().getEleNumber());
				fun.setValue(CommonUtil.nvl(funView.getValue(), "").toString());
				fun.setDisplayExpress(this.replacepDisplayId(funView.getDisplayExpress(),id));
				fun = this.create(fun);
			}
		}
 		return pDisp;
	}
	
	private String replacepDisplayId(String dsp, Long id) {
		if (dsp != null) {
			return dsp.replace(YqConstants.Placeholder.pDisplayId, id.toString());
		}
		return dsp;
	}
	
	private ParseElementDisplay persistParseElementDisplay(ParseElementDisplay parseElementDisplay) {
 		em.persist(parseElementDisplay);
		return parseElementDisplay;
	}
	
	private Long genParseElementDisplayId() {
		return this.yqSequenceService.idNext( ParseElementDisplay.class , 1);
	}
	

	@Override
	public void deleteParseElementDisplay(String modelProperty, Long modelId) {
		ParseElementDisplay pDisp = this.getParseElementDisplay(modelProperty, modelId);
		if (pDisp != null) {
			List<ParseElementDisplayFun> funList = this.find(" from ParseElementDisplayFun where parseElementDisplayId = ?",pDisp.getId());
		    if (CommonUtil.isNotNull(funList)) {
		    	this.delete(funList);
		    }
		    this.delete(pDisp);	
		}
			
	}

	@Override
	public ParseElementDisplay getParseElementDisplay(String modelProperty,
			Long modelId) {
		String query = "select "+JPAUtils.genEntityCols(ParseElementDisplay.class, "a", null)
		+", ele.ele_name funName "
		+" from YQ_PARSE_ELEMENT_DISPLAY a left join YQ_PARSE_ELEMENT ele on a.fun_code = ele.ele_number "
		+" where a.model_id = "+modelId
		+"   and a.model_property = '"+modelProperty+"' ";
		List<ParseElementDisplay> list = this.sqlToModelService.executeNativeQuery(query, null, ParseElementDisplay.class);
		if (CommonUtil.isNotNull(list)) {
			return list.get(0);
		} 
		return null;
	}
	
	
//	/**
//	 * 多条转换成一笔, 只取当前的
//	 * @param modelProperty
//	 * @param modelId
//	 * @return
//	 */
//	@Override
//	public ParseElementDisplay genModelDisplay(String modelProperty,
//			Long modelId) {
//		String query = "select "+JPAUtils.genEntityCols(ParseElementDisplay.class, "a", null)
//		+", ele.ele_name funName "
//		+" from YQ_PARSE_ELEMENT_DISPLAY a "
//		+ " left join YQ_PARSE_ELEMENT ele on a.fun_code = ele.ele_number "
//		+ " left join gc_receivable_dtl_fun dtlfun on a.model_dtl_id = dtlfun.id "
//		+" where a.model_id = "+modelId
//		+"   and a.model_property = '"+modelProperty+"' "
//		+"   and ifnull(dtlfun.use_category,'"+BcConstants.ReceivableDtlFun_UseCategory.CURRENT+"') = '"+BcConstants.ReceivableDtlFun_UseCategory.CURRENT+"' ";
//		List<ParseElementDisplay> list = this.sqlToModelService.executeNativeQuery(query, null, ParseElementDisplay.class);
//		if (CommonUtil.isNotNull(list)) {
//			if (list.size() == 1) {
//				return list.get(0);
//			} else {
//				return this.mergePeList(list);
//			}
//			
//		} 
//		return null;
//	}
//	
//	private ParseElementDisplay mergePeList(List<ParseElementDisplay> list) {
//		ParseElementDisplay retPd = new ParseElementDisplay();
//		retPd.setFunName("月度分段收费");
//		boolean split = true;
//		if ("9999".equals(list.get(0).getFunCode())) {
//			retPd.setFunName("协议调整收费");
//			if (list.size() <= 2) {
//				split = false;
//			}
//		}
//		String splitCh = "";
//		String add = "";
//		int i= 1;
//		for (ParseElementDisplay pd : list) {
//			
//			ReceivableDtlFun dtlFun = this.getReceivableDtlFunById(pd.getModelDtlId());
//			if (dtlFun != null) {
//				if ("9999".equals(pd.getFunCode()) || !split) {
//					String tmp ="<font color='green'>"  
//							 + this.strong(pd.getFunName())+" : "+dtlFun.getJsAmount()
//							 + "</font><br>计算过程：<br>" 
//							 +pd.getDisplayExpress()+" <br><hr><br>";
//							  retPd.setDisplayExpress(CommonUtil.nvl(retPd.getDisplayExpress(),"")+tmp);
//				} else {
//					String tmp ="<font color='green'>"+ this.strong("第 "+i+" 段从 ")+dtlFun.getStartDate().getDate()+"号 " +this.strong("到 ")+dtlFun.getEndDate().getDate()+" 号  "
//							 + this.strong(pd.getFunName())+" : "+dtlFun.getJsAmount() +" * "+dtlFun.getCurrentDays()+"/"+dtlFun.getTotalDays()+" = "+dtlFun.getAmount()
//							 + "</font><br>"+this.strong(pd.getFunName())+" : "+dtlFun.getJsAmount()+"<br> 计算过程：<br>" 
//							 +pd.getDisplayExpress()+" <br><hr><br>";
//							  retPd.setDisplayExpress(CommonUtil.nvl(retPd.getDisplayExpress(),"")+tmp);
//							   i++;			  
//				}
// 				
//			}
// 			retPd.setValue(JsUtil.calculate(new BigDecimal(CommonUtil.nvl(retPd.getValue(),"0").toString()), "+",  dtlFun.getAmount()).toString()); // 一定是金额
// 			splitCh = "/";
//			add = "+";
//		}
//		return retPd;
//	}
//	
//	
// 
	
	private String strong(Object value) {
		return "<strong>"+CommonUtil.nvl(value,"")+"</strong>";
	}
	
	@Override
	public ParseElementDisplayFun getParseElementDisplayFun(
			Long parseElementDisplayId, String funCode) {
		String query = "select "+JPAUtils.genEntityCols(ParseElementDisplayFun.class, "a", null)
				+" , ele.ele_name funName "
				+" from YQ_PARSE_ELEMENT_DISPLAY_FUN a left join YQ_PARSE_ELEMENT ele on a.fun_code = ele.ele_number "
				+" where a.Parse_Element_Display_id = "+parseElementDisplayId
				+"   and a.fun_code = '"+funCode+"' ";
		List<ParseElementDisplayFun> list = this.sqlToModelService.executeNativeQuery(query, null, ParseElementDisplayFun.class);
		if (CommonUtil.isNotNull(list)) {
			return list.get(0);
		}  
		return null;
	}
	
	public List<ParseElement> findFunctionListByUsePropertyName(String useCategory, String useProperty,Map<String,ParseElement> eMap) {
		List<ParseElement> parseElementList = new ArrayList<ParseElement>();
		if (eMap != null) {
			for (ParseElement pe : eMap.values()) {
				if (useProperty.equals(pe.getUsePropertyName())) {
					parseElementList.add(pe);
				}
			}
			return parseElementList;
		}
		
		String sql = "select pe.* "  +
		 		" from yq_parse_element pe where pe.USE_PROPERTY_NAME = '"+useProperty+"' "
		 		+" and pe.USE_CATEGORY = '"+useCategory+"' "
		 		+ " AND pe.ELE_CATEGORY = 'F'";
		parseElementList = this.sqlToModelService.executeNativeQuery(
				sql, null, ParseElement.class);
 
		 return parseElementList;
	}

	@Override
	public String genCalculateResult(ParseElement parseElement) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public String genCalculateResult(ParseElement parseElement) {
//		
//		Map pMap = parseElement.getPageMap();
//		if(CommonUtil.isNull(pMap.get("selectedCMS"))){
//			throw new ValidateException("没选月结数据");
//		}
//		if(CommonUtil.isNull(pMap.get("functionCode"))){
//			throw new ValidateException("没添加参数");
//		}
//		String selectedCMS = parseElement.getPageMap().get("selectedCMS").toString();
//		String[] selectedCMSArr = selectedCMS.split("<=>");
//		String chainId = selectedCMSArr[0];
//		String periodMonth=selectedCMSArr[1];
//		String functionCode = pMap.get("functionCode").toString();
//		String query = " select "
//				+ JPAUtils.genEntityColsForMysql(Chain.class, "c", "chain")
//				+ ","
//				+ JPAUtils.genEntityColsForMysql(ChainMonthSummary.class,
//						"cms", "")
//				+","+JPAUtils.genEntityColsWithPrefix(Contract.class, "contract", "contract")		
//				+ ","
//				+ " cs.status_name \"chain.chainStatusName\", cs.no_receivable \"chain.noReceivable\"  "
//				 
//				+ "  from gc_Chain c left join gc_chain_status cs on cs.status_id = c.chain_status , "
//				+ " GC_CHAIN_MONTH_SUMMARY cms  left join GC_CONTRACT contract on cms.chain_id = contract.chain_id 	"
// 				+ " where  c.id =  cms.chain_id " 
//				+ " and "
//				+ "c.id = "+chainId
//				+ " and "
//				+ "cms.PERIOD_MONTH  = '"+periodMonth
//				+ "' group By  cms.id  ";
//
//		List<ChainMonthSummary> cmsList = this.sqlToModelService
//				.executeNativeQuery(query, null, ChainMonthSummary.class);
//		
//		Set<Long> ids = new HashSet<Long>();
//		for (ChainMonthSummary cms : cmsList) {
//			if (cms.getChain().getNoReceivable() != null && cms.getChain().getNoReceivable()) {
//				throw new ValidateException("客户【"+cms.getChain().getChainName()+"】状态为  ："+cms.getChain().getChainStatusName() + " ,不可以产生应收单！");
//			}
//			cms.setSubmittedBy(YqSecurityUtils.getLoginUserCode());
//			cms.setSubmittedDate(new Date());
//			cms.setStatus("SM");
//			ids.add(cms.getChainId());
//		}
//		Map<Long, Contract> mapByChainId = this.genMonthContract(ids, periodMonth);
//		List<ChainMonthSettlement> settleList = new ArrayList<ChainMonthSettlement>();
//		List<ParseElementDisplayView> parseElementDisplayViews =new ArrayList();
//		for (ChainMonthSummary cms : cmsList) {
//			Contract contract = mapByChainId.get(cms.getChainId());
//			if (contract == null) {
//				throw new ValidateException("不存在 " + periodMonth + "月，客户["
//						+ cms.getChain().getChainName()
//						+ "] 的合同， 可能数据有误，请联系管理员!");
//			}
// 
//			ParseRootAndVariable cmsBean = monthSummaryService.genParseRootAndVariable(cms );
// 			ParseElementDisplayView dv = this.genDisplayViewByEleCodeWithPars(cmsBean,functionCode, null);
//			parseElementDisplayViews.add(dv);
//		}
//		
//		return parseElementDisplayViews.get(0).toString();
//  	}

	/**
	 * 本月的 dtl
	 */
//	private Map<Long, Contract> genMonthContract(Set<Long> chainIds,
//			String month) {
//		String contractQ = " select "
//				+ JPAUtils.genEntityColsForMysql(Contract.class, "c", null)
//				+ " from gc_contract c where "
//				+ CommonUtil.getInIDSql("c.chain_id", chainIds);
//				// + " and c.contract_status != 'CONT_NE' ";
//			    //+ " and c.CAN_BE_RECEIVABLE = 1 "; 测试用固定金额
//		String dtlQ = " select gci.CHARGE_CATEGORY chargeCategory, "
//				+ JPAUtils
//						.genEntityColsForMysql(ContractDtl.class, "dtl", null)
//				+ " from gc_contract c, gc_contract_dtl dtl,gc_charge_item gci "
//				+ " where c.id = dtl.contract_id and gci.charge_code=dtl.charge_code and "
//				+ CommonUtil.getInIDSql("c.chain_id", chainIds) 
//			   // + " and  c.CAN_BE_RECEIVABLE = 1 " //测试阶段， 自己审批后就可以产生应收单
//				//+ " and c.contract_status != 'CONT_NE' "
//			    + " and dtl.period_month = '" + month + "' ";
//				//+ " and (dtl.period_month = '" + month + "' "
//				//		+ " or ( dtl.TIMES_CATEGORY = 'ONLY' and ifNull(dtl.FUNCTION_CODE,'')!= '' and ifNull(dtl.BEEN_RECEIVABLED,0) = 0) ) " ; // 包括一次性的函数
//		List<Contract> list = this.sqlToModelService.executeNativeQuery(
//				contractQ, null, Contract.class);
//		List<ContractDtl> DtlList = this.sqlToModelService.executeNativeQuery(
//				dtlQ, null, ContractDtl.class);
//		Map<Long, Contract> mapById = CommonUtil.ListToMap(list, "id");
//		Map<Long, Contract> mapByChainId = CommonUtil
//				.ListToMap(list, "chainId");
//		for (ContractDtl dtl : DtlList) {
//			mapById.get(dtl.getContractId()).getDtls().add(dtl);
//		}
//		return mapByChainId;
//	}

	@Override
	public Map<String, ParseElement> getElementsByCategory(String category) {
		List<ParseElement> list = this.find(" from ParseElement where useCategory = '"+category+"'");
		return CommonUtil.ListToMap(list, "eleNumber");
	}	
	
	@Override
	public String translationEleCodeWithParsToChiness(String eleCodeWithParas) {
		Object[] oA = this.parseEleCodeWithPars(eleCodeWithParas);
		String eleCode = (String)oA[0];
		 Map<String, ParseElement> eMap= this.getElementsByFunctionCode(eleCode);
		String startCh = "{";
		String endCh = "}";
		String psmStr = eMap.get(eleCode).getEleName();
		Map<String,String> parMap = (Map<String,String>) oA[1];
		if (CommonUtil.isNotNull(parMap)) {
			psmStr=psmStr+"(";
			Iterator it = parMap.keySet().iterator();
			while (it.hasNext()) {
				String pCode = (String)it.next();
				String name = eMap.get(pCode).getEleName();
				String value = parMap.get(pCode);
				psmStr=psmStr+name+" = "+value+",";
			}
			psmStr = psmStr+")";
		}		
		return psmStr;
	}
	
	public ParseElement getEleByCode(String eleNumber) {
		return (ParseElement)this.getSingleRecord(" from ParseElement where eleNumber = '"+eleNumber+"'");
	}
	
	/**
	 * 格式 eleCode(P001=11,P002=12)
	 * Object[0] = eleCode
	 * Object[1] = Map<String,String> 参数
	 * @param eleCodeWithPars
	 * @return
	 */
	@Override
	public Object[] parseEleCodeWithPars(String eleCodeWithPars) {
		Object[] oA = new Object[2];
		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(eleCodeWithPars, "(", ")");
		if (psm.isSplitted()) {
			String pars = psm.getCurrStr().trim();
			if (CommonUtil.isNotNull(pars)) {
				Map<String,String> parMap = new HashMap<String,String>();
				String[] sA = pars.split(",");
				for (String s : sA) {
					String[] pValue = s.split("=");
					if (pValue.length > 2 || pValue.length <1) {
						throw new ValidateException("参数["+eleCodeWithPars+"]格式有误");
					} else if (pValue.length == 2) {
						parMap.put(pValue[0], pValue[1]);
					} else {
						parMap.put(pValue[0], null);
					}
					
				}
				oA[1] = parMap;
			}
			oA[0] =psm.getPreStr();
		} else {
			oA[0] = eleCodeWithPars;
		}
		
		return oA;
	}


}
