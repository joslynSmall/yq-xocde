package com.yq.xcode.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.google.common.base.CaseFormat;
import com.yq.xcode.annotation.EntityColumnTableAlias;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yq.xcode.annotation.ParameterTag;
import com.yq.xcode.common.bean.ParseStrModel;
import com.yq.xcode.common.bean.PropertyChanged;
import com.yq.xcode.common.bean.PropertyDefine;
import com.yq.xcode.common.bean.SelectItem;
import com.yq.xcode.common.exception.ValidateException;
import com.yq.xcode.common.model.LookupCode;
import com.yq.xcode.common.model.PageTag;
import com.yq.xcode.constants.YqSelectHardcodeConstants;
import com.yq.xcode.web.ui.annotation.ColumnLable;

public class YqBeanUtil { 
    private static Log log = LogFactory.getLog(YqBeanUtil.class);
    
    /**
     * 注意， 为了解析方便， 格式代码一定是 - 加 两位字母， 总共 3 位
     */
    public static final Map<String,SelectItem> VALUE_FORMAT = new HashMap<String,SelectItem>(){
    	{
    		this.put("-DX", new SelectItem("#CommonUtil.rmbUppercase","金额大写-DX"));
    		this.put("-PF", new SelectItem("#CommonUtil.percentFormat","百分比格式-PF"));
    		this.put("-SH", new SelectItem("#CommonUtil.convert2ndLineNum","中文序号-SH"));
    	}
    	
    };
	
    
    public static String replaceExpression(Object bean, String str) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
    	if (bean instanceof Map) {
    		((Map)bean).put("CommonUtil", new CommonUtil());
    		simpleContext.setVariables((Map)bean);
    	} else {
    		Map map = new HashMap();
    		map.put("CommonUtil", new CommonUtil());
    		simpleContext.setVariables(map);
    		simpleContext.setRootObject(bean);
    	}
    	ExpressionParser parser= new SpelExpressionParser();  
		
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return psmStr;
    		} else { 
    			String expr = psm.getCurrStr().trim();
    			SelectItem si = getFormatByProperty(expr);
    			if (si != null) {
    				expr = expr.substring(0,expr.length()-3);
    				expr = si.getItemKey()+"("+expr+")";
    			}
    			Expression exp=parser.parseExpression(expr);
    			Object eStr = exp.getValue(simpleContext);
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr.toString();
    			}
    			psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
    		}
    	}
    }
    
    private static SelectItem getFormatByProperty(String property) {
    	if (CommonUtil.isNull(property) || property.length() <=3 ) {
    		return null;
    	}
    	for (String key : VALUE_FORMAT.keySet() ) {
    		if (property.endsWith(key)) {
    			return VALUE_FORMAT.get(key);
    		}
    	}
    	return null;
    }
    
    public static String replaceExpression(Object bean,Map variables, String str) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
    	simpleContext.setRootObject(bean);
    	if (variables != null) {
    		simpleContext.setVariables(variables);
    	} 
    	ExpressionParser parser= new SpelExpressionParser();   
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return psmStr;
    		} else { 
    			Expression exp=parser.parseExpression(psm.getCurrStr().trim());
    			Object eStr = exp.getValue(simpleContext);
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr.toString();
    			}
    			psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
    		}
    	}
    }
    
    
    public static String replaceToValue(String str,Map<String,String> map) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
 
    	String resultStr = "";
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return resultStr+psmStr;
    		} else {
    			String eStr = null;
    			String expr = psm.getCurrStr().trim();
    			SelectItem si = getFormatByProperty(expr);
    			if (si != null ) {
    				String fixStr = expr.substring(expr.length()-3);
    				expr = expr.substring(0,expr.length()-3); 
    				eStr = map.get(expr)+fixStr;
    			} else {
    				eStr = map.get(expr);
    			}  
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr;
    			} else {
    				tmStr = "不存在的属性名称："+psm.getCurrStr().trim();
    			}
    			resultStr = resultStr+psm.getPreStr()+startCh+tmStr+endCh; 
    			psmStr = psm.getPostStr();
    		}
    	}
    }
    
    
    public static String replaceToKey(String str,Map<String,String> map) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
    	Map<String,String> valueToKey = new HashMap<String,String>();
    	for (String key : map.keySet()) {
    		valueToKey.put(map.get(key), key);
    	}
    	String resultStr = "";
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return resultStr+psmStr;
    		} else {
    			String eStr = null;
    			String expr = psm.getCurrStr().trim();
    			SelectItem si = getFormatByProperty(expr);
    			if (si != null) {
    				String fixStr = expr.substring(expr.length()-3);
    				expr = expr.substring(0,expr.length()-3); 
    				eStr = valueToKey.get(expr)+fixStr;
    			} else {
    				eStr = valueToKey.get(expr);
    			} 
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr;
    			} else {
    				throw new ValidateException("不存在的属性显示名称："+psm.getCurrStr().trim());
    			}
    			resultStr = resultStr+psm.getPreStr()+startCh+tmStr+endCh;
    			psmStr = psm.getPostStr();
    		}
    	}
    }
    
    public static byte[] changeFileToByte(File file) throws IOException{
    	if(null!=file){
    		InputStream is=new FileInputStream(file);
    		int length=(int)file.length();
    		if(length>Integer.MAX_VALUE){
    			throw new ValidateException("文件过大！");
    		}
    		byte[] content=new byte[length];
    		int offset=0;
    		int numRead=0;;
    		while(offset<file.length()&&(numRead=is.read(content, offset, content.length-offset))>=0){
    			offset+=numRead;
    		}
    		if(offset<content.length)
            {
               throw new ValidateException("文件错误！");
            }
            is.close();
            return content;
    	}else{
    		throw new ValidateException("文件不存在！");
    	}
    	
    }
    
    public static Object executeExpression(Object bean, String express) {
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
    	if (bean instanceof Map) {
    		((Map)bean).put("CommonUtil", new CommonUtil());
    		simpleContext.setVariables((Map)bean);
    	} else {
    		Map map = new HashMap();
    		map.put("CommonUtil", new CommonUtil());
    		simpleContext.setVariables(map);
    		simpleContext.setRootObject(bean);
    	}
    	ExpressionParser parser= new SpelExpressionParser();   
		Expression exp=parser.parseExpression(express);
		Object result = exp.getValue(simpleContext);
		return result;
    }
    
    public static void Copy(Object source, Object dest)throws Exception {  
        //获取属性  
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);  
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();  
          
        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);  
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();  
          
        try{  
            for(int i=0;i<sourceProperty.length;i++){  
                  
                for(int j=0;j<destProperty.length;j++){  
                      
                    if(sourceProperty[i].getName().equals(destProperty[j].getName())){  
                        //调用source的getter方法和dest的setter方法  
                    destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));  
                        break;                    
                    }  
                }  
            }  
        }catch(Exception e){  
            throw new Exception("属性复制失败:"+e.getMessage());  
        }  
    }  
    
    public static boolean mergeValue(Object dest, Object source)throws Exception {  
        //获取属性  
    	boolean isDifferent=false;
    	
        BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(), java.lang.Object.class);  
        PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();  
          
        BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(), java.lang.Object.class);  
        PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();  
          
        try{  
            for(int i=0;i<sourceProperty.length;i++){  
                  
                for(int j=0;j<destProperty.length;j++){  
                      
                    if(sourceProperty[i].getName().equals(destProperty[j].getName())){  
                        //调用source的getter方法和dest的setter方法  
                    	
                    	if(null!=destProperty[j].getReadMethod().invoke(dest)&&null!=sourceProperty[i].getReadMethod().invoke(source)){
                    		if(!destProperty[j].getReadMethod().invoke(dest).toString().equals(sourceProperty[i].getReadMethod().invoke(source)))
                        	{
                        		isDifferent=true;
                        	}
                    		//destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));  
                    		if(destProperty[j].getPropertyType().equals( String.class)){
                    			
                    			YqBeanUtil.setProperty(dest, destProperty[j].getName(),destProperty[j].getReadMethod().invoke(dest).toString()+"-" +sourceProperty[i].getReadMethod().invoke(source));                    
                    		}
                    	}
                    	break;
                    }  
                }  
            }  
        }catch(Exception e){  
            throw new Exception("属性复制失败:"+e.getMessage());  
        }
		return isDifferent;  
    }  
    
    
    
    public static Object getPropertyValue(Object bean, String property)  {
    		if (CommonUtil.isNull(property)) {
    			return null;
    		}
    		Object value = null;

				try {
					value = PropertyUtils.getProperty(bean, property);
				} catch (NestedNullException e) {

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			return value;
    }
    
    public static Object executeMethod(Object bean,String methodName, Object[] parameters)  {
    	if (CommonUtil.isNull(methodName) || CommonUtil.isNull(methodName.trim())) {
    		return null;
    	}
    	String name = methodName.trim();
    	Method ms = getMethodByParameters(bean, name, parameters );
    	Object resultObj = null;
		try {
			resultObj = ms.invoke(bean, parameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return resultObj;
    	
    }
    
    private static Method getMethodByParameters(Object bean, String name, Object[] parameters )  {
    	if (CommonUtil.isNull(bean)) {
    		return null;
    	}
    	Method[] methods = bean.getClass().getMethods();
    	int pars = 0;
    	if (!CommonUtil.isNull(parameters)) {
    		pars = parameters.length;
    	}
    	Method resultMethod = null;
    	List methodList = new LinkedList();
    	boolean found = false;
    	for (int i=0; i<methods.length; i++) {
    		Method md = methods[i];
    		int tmpPars = 0;
    		Class[] types = new Class[0];
    		if (!CommonUtil.isNull(md.getParameterTypes())) {
        		tmpPars = md.getParameterTypes().length;
        		types = md.getParameterTypes(); 
        	}
    		if (md.getName().equals(name) && pars == tmpPars) {
    			if (resultMethod == null) {
    				resultMethod = md;
    			}
    			methodList.add(md);
    			boolean tmpFound = true;
    			for (int j=0; j<types.length; j++) {
    				Class c = types[j];
    				Object obj = parameters[j];
    				if (obj != null) {
    					if (!obj.getClass().getName().equals(c.getName())){
    						tmpFound = false;
    						break;
    					}
    				}
    			}
    			if (tmpFound) {
    				resultMethod = md;
    				found=true;
    				break;
    			}
    		}
    	}
    	if (!found) {
    		for (int i=0; i<methodList.size(); i++) {
    			Method md = (Method)methodList.get(i);
    			Class[] types = md.getParameterTypes();
    			boolean tmpFound = true;
    			for (int j=0; j<types.length; j++) {
    				Class c = types[j];
    				Object obj = parameters[j];
    				if (obj != null) {
    					if (obj.getClass().getName().equals(c.getName()) || c.getName().equals(Object.class.getName())){
    						
    					} else {
    						tmpFound = false;
    						break;
    					}
    				}
    			}
    			if (tmpFound) {
    				found = true;
    				resultMethod = md;
    				break;
    			}
    		}
    	}
    	if (CommonUtil.isNull(resultMethod)) {
    		String parClass=null;
    		if (!CommonUtil.isNull(parameters)) {
    			String common="";
    			for (int i=0; i<parameters.length; i++) {
    				Object obj = parameters[i];
    				if (obj == null) {
    					parClass = "null"+common;
    				} else {
    					parClass = obj.getClass().getName()+common;
    				}
    				common = ",";
    		 
    			}
    		}
    		System.out.println("Not found method "+name+" with parameter ("+parClass+") in object " + bean.getClass().getName());
    	}
    	return resultMethod;
    }
    public static void setProperty(Object bean, String key, Object value) {
        try {
        	//BeanUtils.setProperty(bean, key, value);
        	YqBeanUtilsEx.setProperty(bean, key, value); // 扩展了日期为空的设定方法， 这样为空就可以了
        	
        }
        catch (Exception e) {
            String mesg = "ERROR: Bean[" + bean + "], key[" + key
                + "] value[" + value + "]";
            log.error(mesg, e);
            e.printStackTrace();
            throw new RuntimeException(mesg, e);
        }
    }
    /*
    public static Object executeExpression(Object bean, String express) {
    	ObjectElement oe = new ObjectElement(bean,express );
    	return oe.getValue();
    }
    */
    
    private static Object executeExpression(Map variables, String express) {
    	StandardEvaluationContext simpleContext = new StandardEvaluationContext();
   		simpleContext.setVariables(variables);
    	ExpressionParser parser= new SpelExpressionParser();   
		Expression exp=parser.parseExpression(express);
		Object result = exp.getValue(simpleContext);
		return result;
    }
    

    
    private static String toStr(Object o) {
    	if (o == null) {
    		return "";
    	}
    	if (o instanceof java.lang.String) {
    		return (String)o;
    	}
    	if (o instanceof java.math.BigDecimal) {
    		Format decimalFormat = new DecimalFormat("###,##0.00");
    		return  decimalFormat.format(o);
    	}
    	if (o instanceof java.util.Date || o instanceof java.sql.Timestamp) {
    		Format dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    		return  dateFormat.format(o);
    	}
    	return o.toString();
   	
    }
    
    public static String replaceExpression(Map variables, String str) {
    	String startCh = "{";
    	String endCh = "}";
    	String psmStr = str;
    	if (CommonUtil.isNull(str)) {
    		return str;
    	}
    	while (true) {
    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
    		if (!psm.isSplitted()) {
    			return psmStr;
    		} else {
    			Object eStr = executeExpression(variables, psm.getCurrStr().trim());
    			String tmStr = "";
    			if (eStr != null) {
    				tmStr = eStr.toString();
    			}
    			psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
    		}
    	}
    }
    
    public static boolean isTrueExpression(Map variables, String express) {
    	if (CommonUtil.isNull(express)) {
    		return true;
    	}
    	String repExpress = replaceExpression(variables, express);
    	StandardEvaluationContext simpleContext= new StandardEvaluationContext(); 
   		simpleContext.setVariables(variables);
		ExpressionParser parser= new SpelExpressionParser();  
		try {
		    Expression exp=parser.parseExpression(repExpress);
			Boolean b = (Boolean)exp.getValue(simpleContext, Boolean.class);
			return b;
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exp --> " + repExpress);
			throw new ValidateException("表达式错误   express : " + express);
		}
		
    }
    
    /**
     * 可以包括{} 括器的对象
     * @param bean
     * @param express
     * @return
     */
    public static Object getValueByExpression(Map variables, String express) {
    	if (CommonUtil.isNull(express)) {
    		return true;
    	}
    	String repExpress = replaceExpression(variables, express);
    	StandardEvaluationContext simpleContext= new StandardEvaluationContext(); 
   		simpleContext.setVariables(variables);
		ExpressionParser parser= new SpelExpressionParser();  
		try {
		    Expression exp=parser.parseExpression(repExpress);
		    Integer b = (Integer)exp.getValue(simpleContext, Integer.class);
			return b;
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidateException("表达式错误 express : " + express);
		}
		
    }
    
    public static BigDecimal calculateExpression(Map variables, String express) {
    	if (CommonUtil.isNull(express)) {
    		return null;
    	}
    	Object obj = getValueByExpression(variables,express);
    	if (CommonUtil.isNull(obj)) {
    		return null;
    	} else {
    		return new BigDecimal(obj.toString());
    	}
    }
    public static String genEntityDefineTableName(Class clazz) {	
    	Table table = (Table)clazz.getAnnotation(Table.class);
    	return table.name();
    }
    
    
    public static Field getFieldIncSuperClazz(Class clazz, String fieldName) {
    	List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
		Class tmpClass = clazz;
		while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	     if (f.getName().equals(fieldName)) {
	    	    	 return f;
	    	     }
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}
		return null;
    }
    public static List<PropertyDefine> genEntityDefine(Class clazz) {	
		List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
		Class tmpClass = clazz;
		while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();    	    	    
	    	    Column column = f.getAnnotation(Column.class);
	    	    JoinColumn loinColumn = f.getAnnotation(JoinColumn.class);
	    	    ColumnLable columnLable = f.getAnnotation(ColumnLable.class);
				EntityColumnTableAlias entityColumnTableAlias = f.getAnnotation(EntityColumnTableAlias.class);

				if (column != null) {
	    	    	PropertyDefine pd = new PropertyDefine();
	    	    	pd.setProperty(f.getName());
	    	    	pd.setColumn(column.name());
	    	    	pd.setDataType(f.getType().getName());
	    	    	if (CommonUtil.isNotNull(columnLable)) {
	    	    		pd.setLable(columnLable.name());
	    	    	}
	    	    	pdList.add(pd);
	    	    } else if (loinColumn != null ) {
	    	    	PropertyDefine pd = new PropertyDefine();
	    	    	pd.setProperty(f.getName());
	    	    	pd.setColumn(loinColumn.name());
	    	    	pd.setDataType(f.getType().getName());
	    	    	pd.setLoinColumn(true);
	    	    	if (CommonUtil.isNotNull(columnLable)) {
	    	    		pd.setLable(columnLable.name());
	    	    	}
	    	    	pdList.add(pd);
	    	    } else if(entityColumnTableAlias != null){
					PropertyDefine pd = new PropertyDefine();
					pd.setProperty(f.getName());
					String columnAliasc = entityColumnTableAlias.columnAliasc();
					pd.setColumn(entityColumnTableAlias.tableAlias()+"."+("".equals(columnAliasc)? CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, f.getName()):columnAliasc));
					pd.setDataType(f.getType().getName());
					pd.setLoinColumn(true);
					if (CommonUtil.isNotNull(columnLable)) {
						pd.setLable(columnLable.name());
					}
					pdList.add(pd);
				}
	    	}
			tmpClass = tmpClass.getSuperclass();
		}
    	return pdList;
	}
    
    public static List<Field> getFieldsByAnnotation(Class clazz, Class annotationClazz) {
    	Class tmpClass = clazz;
    	List<Field> fieldList = new ArrayList<Field>();
    	while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();     	    
	    	    Annotation c = f.getAnnotation(annotationClazz);
	    	    if (c != null) {
	    	    	fieldList.add(f);
	    	    }
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}  
    	return fieldList;
    }
    
    public static List<PropertyDefine> genPropertyDefineByColumnLable(Class clazz) {	
		List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
		Class tmpClass = clazz;
		if (clazz == null) {
			return pdList;
		}
		if (clazz.isInterface()) {
			Method[] methods = tmpClass.getDeclaredMethods();
			for (Method m : methods)   {
				if(m.getName().startsWith("get") && CommonUtil.isNull(m.getParameterTypes())) {
					ColumnLable lable = m.getAnnotation(ColumnLable.class);
					if(lable != null) {
					    String proName = m.getName().substring(3,4).toLowerCase() + m.getName().substring(4);
					    Class type = m.getReturnType();					
						PropertyDefine pd = new PropertyDefine();
						pd.setLable(lable.name());
						pd.setDataType(type.getName());
						pd.setProperty(proName);
						pd.setLineNum(lable.lineNum());
						pdList.add(pd);
					}
				}
			}
			
		} else {
			while (tmpClass != null) {
				Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
				for (Field f : fs)   {   
		    	    Class fieldClazz = f.getType();     	    
		    	    ColumnLable column = f.getAnnotation(ColumnLable.class);
		    	    if (column != null) {
		    	    	PropertyDefine pd = new PropertyDefine();
		    	    	pd.setProperty(f.getName());
		    	    	pd.setColumn(column.name());
		    	    	pd.setLable(column.name());
		    	    	pd.setDataType(fieldClazz.getName());
		    	    	pd.setLineNum(column.lineNum());
		    	    	pdList.add(pd);
		    	    }
		    	}  
				tmpClass = tmpClass.getSuperclass();
			}    
		}
		Collections.sort(pdList,  new Comparator<PropertyDefine>() {
			public int compare(PropertyDefine s1, PropertyDefine s2) {
				return s1.getLineNum().compareTo( s2.getLineNum());
			}
		});
    	return pdList;
	}
    
    public static List<PropertyDefine> genPropertyDefine(Class clazz) {	
		List<PropertyDefine> pdList = new ArrayList<PropertyDefine>();
		Class tmpClass = clazz;
		if (clazz.isInterface()) {
			Method[] methods = tmpClass.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().startsWith("get") && CommonUtil.isNull(m.getParameterTypes())) {
 					String proName = m.getName().substring(3, 4).toLowerCase() + m.getName().substring(4);
					Class type = m.getReturnType();
					PropertyDefine pd = new PropertyDefine();
					pd.setDataType(type.getName());
					pd.setProperty(proName);
					pdList.add(pd);
					ColumnLable lable = m.getAnnotation(ColumnLable.class);
					if (lable != null) {
						pd.setLable(lable.name());
						pd.setLineNum(lable.lineNum());
					}
				}
			}
			
		} else {
			while (tmpClass != null) {
				Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
				for (Field f : fs)   {   
		    	    Class fieldClazz = f.getType();   
		    	    PropertyDefine pd = new PropertyDefine();
	    	    	pd.setProperty(f.getName());
 	    	    	pd.setDataType(fieldClazz.getName());
	    	    	pdList.add(pd);
		    	    ColumnLable column = f.getAnnotation(ColumnLable.class);
		    	    if (column != null) {
 		    	    	pd.setColumn(column.name());
		    	    	pd.setLable(column.name());
		    	    	pd.setLineNum(column.lineNum());
 		    	    }
		    	}  
				tmpClass = tmpClass.getSuperclass();
			}    
		}
		Collections.sort(pdList,  new Comparator<PropertyDefine>() {
			public int compare(PropertyDefine s1, PropertyDefine s2) {
				return s1.getLineNum().compareTo( s2.getLineNum());
			}
		});
    	return pdList;
	}
    
    public static String jsonToString(Object jsonObject) {
    	ObjectMapper om = new ObjectMapper();
    	String jsonStr = null;
		try {
			jsonStr = om.writeValueAsString(jsonObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return jsonStr;
    }
    public static <E extends Object> E StringToJson(String jsonString,  Class<E> clazz) {
    	ObjectMapper om = new ObjectMapper();
    	try {
			return om.readValue(jsonString, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
	/**
	 * exCludePropertyName 不合并的属性, 五个标准属性不合并 id,version,createUser,createTime,lastUpdateUser,lastUpdateTime
	 * 根据toEntity 做属性参考, 参考的class 
	 */
	
	public static <E> E mergeToEntity(Object fromEntity, E toEntity, Class referClazz, String exCludePropertyName) {
		String exPs = ","+exCludePropertyName+",";

		List<PropertyDefine> pdList = YqBeanUtil.genEntityDefine(referClazz);
		for (PropertyDefine pd : pdList) {
			if (!exPs.contains(","+pd.getProperty()+",")) {
				Object value = YqBeanUtil.getPropertyValue(fromEntity, pd.getProperty());
				setBeanProperty(toEntity, pd.getProperty(), value);
				//YqBeanUtil.setProperty(toEntity, pd.getProperty(), value);
			}
		}
		return toEntity;
	}
	
	/**
	 * inCludePropertyName 需要合并的属性
	 * 根据toEntity 做属性参考, 参考的class 
	 */
	
	public static <E> E mergeToEntityInclude(Object fromEntity, E toEntity, Class referClazz, String inCludePropertyName) {
		String inPs = ","+inCludePropertyName+",";
		
		List<PropertyDefine> pdList = YqBeanUtil.genEntityDefine(referClazz);
		for (PropertyDefine pd : pdList) {
			if (inPs.contains(","+pd.getProperty()+",")) {
				Object value = YqBeanUtil.getPropertyValue(fromEntity, pd.getProperty());
				setBeanProperty(toEntity, pd.getProperty(), value);
				//YqBeanUtil.setProperty(toEntity, pd.getProperty(), value);
			}
		}
		return toEntity;
	}
	
	public static List<PropertyChanged> getPropertyChanged(Object oldEntity, Object newEntity, Class referClazz, String exCludePropertyName) {
		String exPs = ""+exCludePropertyName;
		
		List<PropertyDefine> pdList = YqBeanUtil.genPropertyDefineByColumnLable(referClazz);
		List<PropertyChanged> pcList = new ArrayList<PropertyChanged>();
		for (PropertyDefine pd : pdList) {
			if (!exPs.contains(","+pd.getProperty()+",")) {
				Object oldValue = YqBeanUtil.getPropertyValue(oldEntity, pd.getProperty());
				Object newValue = YqBeanUtil.getPropertyValue(newEntity, pd.getProperty());
				if (!CommonUtil.isEquals(oldValue, newValue)) {
					PropertyChanged pc = new PropertyChanged();
					pc.setProperty(pd.getProperty());
					pc.setPropertyName(pd.getLable());
					pc.setNewValue(newValue);
					pc.setOldValue(oldValue);
					pcList.add(pc);					
				}
			}
		}
		return pcList;
	}
	
	 public static String replaceExpressionForEmail(Object bean, String str) {
	    	String startCh = "{";
	    	String endCh = "}";
	    	String psmStr = str;
	    	if (CommonUtil.isNull(str)) {
	    		return str;
	    	}
	    	while (true) {
	    		ParseStrModel psm = CommonUtil.parseStrByStardEndCh(psmStr, startCh, endCh);
	    		if (!psm.isSplitted()) {
	    			return psmStr;
	    		} else {
	    			Object eStr = executeExpression(bean, psm.getCurrStr().trim());
	    			String tmStr = toStr(eStr);
	    			psmStr = psmStr.replace(startCh+psm.getCurrStr()+endCh, tmStr);
	    		}
	    	}
	  }
	 /**
	  * 取 type 类型 的 class 
	  * @param field
	  * @return
	  */
	public static Class genGenericTypeClass(Field field) {
			String s = field.getGenericType().toString();
			String modelName = s.substring(s.indexOf("<")+1,s.length()-1);
			if (CommonUtil.isNull(modelName)) {
				return null;
			}
			if (modelName.contains("<")) {
				return null;
			}
			try {
				return Class.forName(modelName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return null;
	}
	
	public  static <E> List<E> JsonStrToObjList(String jsonStr, Class<E> clazz ) {
		if (CommonUtil.isNull(jsonStr)) {
			return null;
		}
		List<E> list = new ArrayList<E>();
		JSONArray jArr = JSONArray.parseArray(jsonStr);
		for (int i=0; i<jArr.size(); i++ ) {
			JSONObject jObj = jArr.getJSONObject(i);
			E obj = (E)JSONObject.toJavaObject(jObj, clazz);
			list.add(obj);
		}
		return list;
	}
	
	
	
	public static Object toPropertyValue(String propertyType, Object value) {
		if (CommonUtil.isNull(value)) {
			return getNvlValue(propertyType);
		}
		if ( value.getClass().getName().equals(propertyType)) {
			return value;
		}
		if ("java.lang.String".equals(propertyType)) {
			return value.toString();
		} else if ("java.util.Date".equals(propertyType)) {
			return DateUtil.convertStringToDateForAll(value.toString());
		} else if ("java.math.BigDecimal".equals(propertyType)) {
			return new BigDecimal(value.toString());
		} else if ("java.lang.Integer".equals(propertyType) || "integer".equals(propertyType)) {
			return new BigDecimal(value.toString()).intValue();
		} else if ("java.lang.Double".equals(propertyType) || "double".equals(propertyType)) {
			return new Double(value.toString());
		}   else if ("java.lang.Float".equals(propertyType) || "float".equals(propertyType)) {
			return new Float(value.toString());
		}   else if ("java.lang.Short".equals(propertyType) || "short".equals(propertyType)) {
			return new Short(value.toString());
		}  else if ("java.lang.Long".equals(propertyType) || "long".equals(propertyType)) {
			return new Long(value.toString());
		}   else if ("boolean".equals(propertyType) || "java.lang.Boolean".equals(propertyType)) {
			return new Boolean(value.toString());
		} else {
			return value;
		}  
	}
	 
	private static Object  getNvlValue(String classType) {
       if ("integer".equals(classType)) {
			return Integer.valueOf(0);
		} if ("double".equals(classType)) {
			return Double.valueOf(0);
		} else  if ("float".equals(classType)) {
			return Float.valueOf(0);
		} else if ("short".equals(classType)) {
			return Short.valueOf("0");
		}  else if ("long".equals(classType)) {
			return 0l;
		}  else if ("boolean".equals(classType)) {
			return false;
		} 
		return null;
	}
	
	public static void setBeanProperty(Object model, String propertyName, Object value) {
		String[] pros = propertyName.split("\\.");
		try {
			Object parObj = model;
			for (int i=0; i<pros.length - 1; i++) {
				String proName = pros[i];
				Object pro = PropertyUtils.getProperty(parObj, proName);
				if (pro == null) {
					Class id = PropertyUtils.getPropertyDescriptor(parObj, pros[i]).getPropertyType();
					pro = id.newInstance();
					PropertyUtils.setProperty(parObj, proName, pro);
				}
				parObj = pro;
			}
			PropertyUtils.setProperty(parObj, pros[pros.length - 1], value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("",e);
		}
	}
	
	/**
	 * 自动转换
	 * @param model
	 * @param propertyName
	 * @param value
	 */
	public static void setBeanPropertyAuto(Object model, String propertyName, String propertyType, Object value) {
		String[] pros = propertyName.split("\\.");
		try {
			Object parObj = model;
			for (int i=0; i<pros.length - 1; i++) {
				String proName = pros[i];
				Object pro = PropertyUtils.getProperty(parObj, proName);
				if (pro == null) {
					Class id = PropertyUtils.getPropertyDescriptor(parObj, pros[i]).getPropertyType();
					pro = id.newInstance();
					PropertyUtils.setProperty(parObj, proName, pro);
				}
				parObj = pro;
			}
			PropertyUtils.setProperty(parObj, pros[pros.length - 1],  toPropertyValue(propertyType,value));
		} catch (Exception e) {
			log.error("",e);
			throw new ValidateException("类型转换错误");
			
		}
	}
	
    /**
     * 
     * @param dest
     * @param source
     * @param fromToPropertyMap  key 是 from 的属性， value 是 to的属性
     */
    public static void mergeValue(Object fromObj, Object toObj,Map<String,String> fromToPropertyMap) {
    	mergeValue(fromObj, toObj, fromToPropertyMap, null);
    }
    
    public static void mergeValue(Object fromObj, Object toObj,Map<String,String> fromToPropertyMap, String dateFormat) {
    	if (fromObj == null || toObj == null || fromToPropertyMap == null) {
    		return;
    	}
    	Field[] fs = toObj.getClass().getDeclaredFields(); // 得到所有的fields 
    	Map<String,Class> toPropertyTypeMap = new HashMap<String,Class>();
		for (Field f : fs)   {   
    	    Class fieldClazz = f.getType();  
    	    toPropertyTypeMap.put(f.getName(), fieldClazz);
		}
    	for (String fromProperty : fromToPropertyMap.keySet()) {
    		Object fromValue = YqBeanUtil.getPropertyValue(fromObj, fromProperty);
    		String toProperty = fromToPropertyMap.get(fromProperty);
    		Class type = toPropertyTypeMap.get(toProperty);
    		if (type == null) {
    			throw new ValidateException(" 不存在的 toProperty 属性名称 -->"+toProperty);
    		}
    		Object value = convertByType(fromValue,type, dateFormat);
    		YqBeanUtil.setProperty(toObj, toProperty, value);
    	} 
    }
    
    /**
     * 自定义字段转换用， 日期格式为   yyyy/MM/dd mm:ss:
     * @param value
     * @param type
     * @return
     */
    
    private static Object convertByType(Object value, Class type, String dateFormat) {
    	String nDateFormat = CommonUtil.isNull(dateFormat) ? "yyyy/MM/dd HH:mm:ss" : dateFormat;
    	String classType = type.getName();
    	if (value == null) {
    		return getNvlValue(classType);
    	}
    	if (value instanceof String) {
    		Object retValue = getNvlValue(classType);
    		if (retValue != null) {
    			return retValue;
    		}
		} 
    	if (value.getClass().getName().equals(classType)) {
    		return value;
    	}
 
		PropertyDescriptor property = null;
		if ( type.isEnum()) { //
			throw new ValidateException("现在不支持enum 属性!");
			
//			Enumerated em = field.getAnnotation(Enumerated.class);
//			if (em != null && EnumType.STRING.compareTo(em.value()) == 0 ) {
//				return Enum.valueOf(field.getType().asSubclass(Enum.class), fieldValue.toString());
//			} else {
//				Object[] emSa = field.getType().getEnumConstants();
//				if (CommonUtil.isNotNull(emSa)) {
//					Integer ind = Integer.parseInt(fieldValue.toString());
//					return emSa[ind];
//				}
//			}
		} else	if ("java.lang.String".equals(classType)) {
			if (value instanceof Date) {
				return DateUtil.convertDate2String((Date)value,nDateFormat);
			}
			return value.toString();
		} else if ("java.util.Date".equals(classType)) { // 现在支持默认格式
			//return DateUtil.convertString2Date(value.toString(),dateFormat);
			if (CommonUtil.isNull(dateFormat)) {
				return DateUtil.convertString2DateAnyFmt(value.toString());
			} else {
				return DateUtil.convertString2Date(value.toString(),dateFormat);
			}
		} else if ("java.lang.Integer".equals(classType) || "integer".equals(classType)) {
			return  new BigDecimal(value.toString()).intValue(); 
		}  else if ("java.lang.Double".equals(classType) || "double".equals(classType)) {
			return Double.parseDouble(value.toString());
		}   else if ("java.lang.Float".equals(classType) || "float".equals(classType)) {
			return Float.parseFloat(value.toString());
		}   else if ("java.lang.Short".equals(classType) || "short".equals(classType)) {
			return Short.parseShort(value.toString());
		}   else if ("java.lang.Long".equals(classType) || "long".equals(classType)) {
			return Long.parseLong(value.toString());
		}  else if ("java.math.BigDecimal".equals(classType)) {
			return new BigDecimal(value.toString());
		} else if ("boolean".equals(classType) || "java.lang.Boolean".equals(classType)) {
			return convertToBoolean(value);
		} else {
			return value;
		} 
    }
    
    private static Boolean convertToBoolean(Object obj) {
    	if (obj == null) {
    		return false;
    	}
    	String value = obj.toString().toUpperCase();
    	if ("Y".equals(value) || "1".equals(value) || "TRUE".equals(value) || "是".equals(value)) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public static <E> E cloneBean(E bean) {
    	try {
			return (E) BeanUtils.cloneBean(bean);
		} catch ( Exception e) { 
			e.printStackTrace();
			throw new ValidateException("cloneBean 出错 ！" +bean.getClass().getName());
		}  
    }
    
 
    public static boolean isTrueExpression(Object bean, String express) {
    	if (CommonUtil.isNull(express)) {
    		return true;
    	}
    	String repExpress = replaceExpression(bean, express);
    	StandardEvaluationContext simpleContext= new StandardEvaluationContext(bean); 
    		Map map = new HashMap();
    		map.put("CommonUtil", new CommonUtil());
    		simpleContext.setVariables(map);
    		simpleContext.setRootObject(bean);
		ExpressionParser parser= new SpelExpressionParser();  
		try {
		    Expression exp=parser.parseExpression(repExpress);
			Boolean b = (Boolean)exp.getValue(simpleContext, Boolean.class);
			return b;
		} catch (ValidateException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exp --> " + repExpress);
			throw new ValidateException("表达式错误,Bean : "+bean.getClass().getName()+" express : " + express);
		}
		
    }	
    
    /**
     * 
     * @param clazz
     * @return
     */
    public static List<PageTag> genPageTagList(Class clazz ) {
    	List<PageTag> ptList = new ArrayList<PageTag>();
    	List<Field> fList = getFieldsByAnnotation(clazz, ParameterTag.class) ;
    	if ( fList != null ) {
    		for ( Field f : fList ) {
    			ParameterTag pTag = f.getAnnotation(ParameterTag.class);
    			PageTag pt = new PageTag();
    			pt.setTagKey(pTag.tagKey());
    			if (CommonUtil.isNull(pTag.tagKey())) {
    				if ( CommonUtil.isNotNull(pTag.endProperty()) && "java.util.Date".equals(f.getType().getName())) { // 是日期, 又有结束属性, 必然是期间选择
    					SelectItem rangDate = CommonUtil.getSelectItemByKey(YqSelectHardcodeConstants.PageTag_tag.list, YqSelectHardcodeConstants.PageTag_tag.DateRangeTag)  ;
    					pt.setTagKey(rangDate.getItemKey());
    				} else {
    					pt = YqSelectHardcodeConstants.PageTag_tag.getDefaultPageTagByType(f.getType().getName());
    					if ( pt == null ) {
    						throw new ValidateException(f.getType().getName()+" 没有指定末日控件 ，请联系架构部 ！ ");
    					}
    				}
    				
    			} 
     			pt.setProperty(f.getName());
    			pt.setLineNumber(pTag.lineNumber()); 
    			pt.setEndProperty(pTag.endProperty());
     			pt.setLable(pTag.lable());
    			pt.setPostLable("后缀标题");
    			pt.setListCategory(pTag.listCategory());
    			pt.setValue(pTag.defaultValue());
    			pt.setPlaceHolder(pTag.placeHolder());
    			ptList.add(pt);
    		}
    		ptList.sort(new Comparator<PageTag>() {
 				@Override
				public int compare(PageTag pt1, PageTag pt2) {
 					return pt1.getLineNumber().compareTo( pt2.getLineNumber()); 
				} 
    		});
    		
    	}
    	
    	return ptList;
    }
    
    /**
     * 全部字段定义
     * ColumnLable 列定义 
     * itemKey : property
     * itemName : lable
     * itemValue : propertyType
     */
    public static List<SelectItem> genAllDefinePropertyList(Class clazz ) {
    	List<SelectItem> pdList = new ArrayList<SelectItem>();
		Class tmpClass = clazz;
		while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();   
	    	    String type = null;
	    	    if (fieldClazz != null ) {
	    	    	type = fieldClazz.getName();
	    	    } 
	    	    ColumnLable columnLable = f.getAnnotation(ColumnLable.class); 
				if (columnLable != null && !columnLable.canNotDefine() ) {
	    	    	 SelectItem si = new SelectItem(f.getName(),columnLable.name(),type,null);
	    	    	 pdList.add(si);
 				}
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}    	
    	return pdList;
    }
    
    /**
     * 扩展字段定义
     * ColumnLable 列定义 
     * itemKey : property
     * itemName : lable
     * itemValue : propertyType
     */
    public static List<SelectItem> genExtDefinePropertyList(Class clazz ) {
    	List<SelectItem> pdList = new ArrayList<SelectItem>();
		Class tmpClass = clazz;
		while (tmpClass != null) {
			Field[] fs = tmpClass.getDeclaredFields(); // 得到所有的fields  
			for (Field f : fs)   {   
	    	    Class fieldClazz = f.getType();   
	    	    String type = null;
	    	    if (fieldClazz != null ) {
	    	    	type = fieldClazz.getName();
	    	    } 
	    	    ColumnLable columnLable = f.getAnnotation(ColumnLable.class); 
				if (columnLable != null && !columnLable.canNotDefine() && columnLable.extProperty() ) {
	    	    	 SelectItem si = new SelectItem(f.getName(),columnLable.name(),type,null);
	    	    	 pdList.add(si);
 				}
	    	}  
			tmpClass = tmpClass.getSuperclass();
		}    	
    	return pdList;
    }
    
    public static boolean endsWith(String s1, String s2 ) {
    	if ( s1 == null && s2 == null ) {
    		return false;
    	}
    	return s1.toLowerCase().endsWith(s2.toLowerCase());
    }
	
	

  
    public static void main(String[] arg) {
    	List<SelectItem> pdList = genAllDefinePropertyList(LookupCode.class);
    	for ( SelectItem si : pdList ) {
    		System.out.println(si.getItemKey()+" --> " + si.getItemName());
    	} 
    }

}
