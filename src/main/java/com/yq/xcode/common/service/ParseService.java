package com.yq.xcode.common.service;

import java.util.List;
import java.util.Map;

import com.yq.xcode.common.bean.ContractFunctionView;
import com.yq.xcode.common.bean.ParseElementDisplayView;
import com.yq.xcode.common.bean.ParseFunction;
import com.yq.xcode.common.bean.ParseParameter;
import com.yq.xcode.common.model.ParseElement;
import com.yq.xcode.common.model.ParseElementDisplay;
import com.yq.xcode.common.model.ParseElementDisplayFun;


public interface ParseService {

	/**
	 * 包括参数和自定义数据,
	 */
	public Object executeElementExpression(Object bean, String express, 
			Map<String, ParseElement> elements,Map<String, String> newParas);

	public Boolean isTrueElementExpression(Object bean, String express, 
			 Map<String, ParseElement> elements,Map<String, String> newParas);

 
	/**
	 * 
	 * 根据元素的定义产生元素的值
	 * @param bean -- 对象
	 * @param eleCode -- 元素代码， 包括因子，参数， 函数, 还可以附带参数，根式为eleCode(P001=123,P002=234)
	 * @param simpleContext -- 解析上下文， 主要为批量循环调用时， 提供效率
	 * @param variables 替代的系统参数，
	 * @param element 所有元素， 为提高效率用
	 * @return
	 */
	public Object genValueByEleNumber(Object bean, String eleCode,
			 Map<String, ParseElement> element,Map<String, String> newParas);
	/**
	 * 
	 * @param bean
	 * @param eleCodeWithPars 元素代码， 包括因子，参数， 函数, 还可以附带参数，根式为eleCode(P001=123,P002=234)
	 * @param element
	 * @return
	 */
	public Object genValueByEleNumberWithPars(Object bean, String eleCodeWithPars,
			 Map<String, ParseElement> element);
	
	/**
	 * 
	 * 根据元素的定义产生元素的值, 返回的值一定是Boolean
	 * @param bean -- 对象
	 * @param eleCode -- 元素代码， 包括因子，参数， 函数 函数,
	 * @param simpleContext -- 解析上下文， 主要为批量循环调用时， 提供效率
	 * @param variables 替代的系统参数，
	 * @param element 所有元素， 为提高效率用
	 * @return
	 */
	public Boolean isTrueByEleNumber(Object bean, String eleCode,
			  Map<String, ParseElement> elements, Map<String, String> newParas);
	/**
	 * 
	 * @param bean
	 * @param eleCodeWithPars 元素代码， 包括因子，参数， 函数  还可以附带参数，根式为eleCode(P001=123,P002=234)
	 * @param elements
	 * @return
	 */
	public Boolean isTrueByEleNumberWithPars(Object bean, String eleCodeWithPars,
			  Map<String, ParseElement> elements);
	
	
	
	
	/**
	 * 将表达式的值{eleNumber}替换成{eleName:value}，包括计算值，还有取数来源
	 */
	public ParseElementDisplayView genDisplayViewByEleCode(Object bean, String eleCode, 
			Map<String, ParseElement> elements, Map<String, String> newParas);
	/**
	 * 将表达式的值{eleNumber}替换成{eleName:value}，包括计算值，还有取数来源
	 */
	public ParseElementDisplayView genDisplayViewByEleCode(Object bean, String eleCode, 
			Map<String, ParseElement> elements, Map<String, String> newParas,String unitParStr);
	/**
	 * 将表达式的值{eleNumber}替换成{eleName:value}，包括计算值，还有取数来源,
	 * 包括因子，参数， 函数  还可以附带参数，根式为eleCode(P001=123,P002=234)
	 */
	public ParseElementDisplayView genDisplayViewByEleCodeWithPars(Object bean,  String eleCodeWithPars, Map<String, ParseElement> elements);

	/**
	 * 将表达式的值{eleNumber}替换成{eleName:value}，包括计算值，还有取数来源,
	 * 包括因子，参数， 函数  还可以附带参数，根式为eleCode(P001=123,P002=234)
	 * unitParStr, 打开元素的列表
	 */
	public ParseElementDisplayView genDisplayViewByEleCodeWithPars(Object bean,  String eleCodeWithPars, Map<String, ParseElement> elements,String unitParStr);

	/**
	 * 将表达式的值{eleNumber}替换成{eleName}
	 */
	public String translationChiness(ParseFunction function, Map<String,ParseElement> eMap );
	
	/**
	 * 将元素包括参数转换为中文
	 */
	public String translationEleCodeWithParsToChiness(String eleCodeWithParas,  Map<String,ParseElement> eMap );
	
	
 
	
	/**
	 * 查找函数的参数列表，如果有新值，设置参数的新值（newValue）,
	 * functionCodeWithPars 格式F001(P001=123,P002=234)
	 */
	public List<ParseParameter> findAndResetFunctionParameters(String functionCodeWithPars, Map<String,ParseElement> eMap);
	
	/**
	 * 如果新值非空， 压缩格式为（P001=123,P002=234）
	 * @param parameters
	 * @return
	 */
	public String zipParametersNewValue(List<ParseParameter> parameters);

	public Map<String, ParseElement> getElementsByFunctionCode(String functionCode);
	/**
	 * 
	 * @param eleNumber
	 * @return
	 */
	public ParseElement getEleByCode(String eleNumber,Map<String,ParseElement> eMap);
	
	/**
	 * 测试语法， 如果有问题以ParseExprExcepton的形式抛出去
	 * @param bean
	 * @param function
	 * @return
	 */
	public ParseElementDisplayView genTestDisplayViewByFunction(ParseElement function);
	
	/**
	 * F001(P001=123,P002=234)
	 */
	public ContractFunctionView genConFunctionCodeFormat(String functionCode,Map<String, ParseElement> elements);
	
	public ParseElementDisplay createParseElementDisplay(String modelProperty,Long modelId,Long modelDtlId, ParseElementDisplayView pView);
	public void deleteParseElementDisplay(String modelProperty,Long modelId);
	public ParseElementDisplay getParseElementDisplay(String modelProperty,Long modelId);
	public ParseElementDisplayFun getParseElementDisplayFun(Long parseElementDisplayId,String funCode);

	public List<ContractFunctionView> genConFunctionCodeFormatList(
			String functionCode, Map<String, ParseElement> eMap);

	public Map<String, ParseElement> getElementsByUseCategory(String useCategory);
	 
	/**
	 * 根据使用属性查找
	 * @param chargeCode
	 * @param eMap
	 * @return
	 */
	public List<ParseElement> findFunctionListByUsePropertyName(String useCategory, String useProperty,Map<String,ParseElement> eMap);

	public String genCalculateResult(ParseElement parseElement);

	public List<ParseElement> getElementslistByUseCategory(String usecategory);

	//public ParseElementDisplay genModelDisplay(String modelProperty, Long modelId); 
	
	public List<String[]> getParamsCn(String functionCode,Map<String, ParseElement> eMap);
	
	
	/**
	 * 包括ParseElement,ParseFunction,ParseParameter数据，替换中文，替换表达式用
	 * key = ParseEleView.eleNumber
	 * @return
	 */
	public Map<String, ParseElement> getElementsByCategory(String category) ;
	
	public ParseElement getEleByCode(String eleNumber);

	public String translationEleCodeWithParsToChiness(String eleCodeWithParas);

	public Object[] parseEleCodeWithPars(String eleCodeWithPars);
}
