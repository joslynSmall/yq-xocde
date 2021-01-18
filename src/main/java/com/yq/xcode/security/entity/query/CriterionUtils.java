package com.yq.xcode.security.entity.query;

import java.lang.reflect.Array;
import java.util.Collection;

import org.springframework.util.StringUtils;

public class CriterionUtils {

public final static String OPERATOR_LIKE = "like";
	
	public final static String OPERATOR_EQUALS = "=";
	
	public final static String OPERATOR_NOT_EQUALS = "!=";
	
	public final static String OPERATOR_IN = "in";
	
	public final static String OPERATOR_NOT_IN = " not in";
	
	public static SimpleCriterion equals(String propName,Object value,boolean ignoreWhenNull) {
		if(ignoreWhenNull && isNullOrEmpty(value) ) {
			return null;
		}
		return new SimpleCriterion(propName,"=",value,ignoreWhenNull);
	}
	
	public static SimpleCriterion notEquals(String propName,Object value ,boolean ignoreWhenNull) {
		if(ignoreWhenNull && isNullOrEmpty(value) ) {
			return null;
		}
		return new SimpleCriterion(propName,"!=",value,ignoreWhenNull);
	}
	
	public static InCriterion inOpr(String propName,Object value ,boolean ignoreWhenNull) {
		if(ignoreWhenNull && isNullOrEmpty(value) ) {
			return null;
		}
		return new InCriterion(propName,"in",value,ignoreWhenNull);
	}
	
	public static InCriterion NotInOpr(String propName,Object value ,boolean ignoreWhenNull) {
		if(ignoreWhenNull && isNullOrEmpty(value) ) {
			return null;
		}
		return new InCriterion(propName," not in",value,ignoreWhenNull);
	}
	
	public static boolean isNullOrEmpty(Object value) {
		if(value == null) {
			return true;
		}
		if(value instanceof Collection) {
			return (((Collection)value).size() == 0);
		}
		if(value.getClass().isArray()) {
			return Array.getLength(value) ==0;
		}
		if(value instanceof String) {
			return !StringUtils.hasText((String)value);
		}
		return false;
	}
	
	public static SimpleCriterion startWith(String propName,String pattern,boolean ignoreWhenNull) {
		if(!StringUtils.hasText(pattern) && ignoreWhenNull) {
			return null;
		}
		return new SimpleCriterion(propName,"like",toStartWithLikeString(pattern),ignoreWhenNull);
	}
	
	public static SimpleCriterion endWith(String propName,String pattern,boolean ignoreWhenNull) {
		if(!StringUtils.hasText(pattern) && ignoreWhenNull) {
			return null;
		}
		return new SimpleCriterion(propName,"like",toEndWithLikeString(pattern),ignoreWhenNull);
	}
	
	public static SimpleCriterion contains(String propName,String pattern,boolean ignoreWhenNull) {
		if(!StringUtils.hasText(pattern) && ignoreWhenNull) {
			return null;
		}
		return new SimpleCriterion(propName,"like",toContainsLikeString(pattern),ignoreWhenNull);
	}
	
	
	public static BetweenCriterion between(String propName,Object value1,Object value2) {
		if(isNullOrEmpty(value1) && isNullOrEmpty(value2)) {
			return null;
		}
		return new BetweenCriterion(propName,value1,value2,true,true);
	}
	
	public static BetweenCriterion between(String propName,Object value1,Object value2,boolean includeStartValue,boolean includeEndValue) {
		if(isNullOrEmpty(value1) && isNullOrEmpty(value2)) {
			return null;
		}
		return new BetweenCriterion(propName,value1,value2,includeStartValue,includeEndValue);
	}
	
	public static String toEndWithLikeString(String text) {
		if(StringUtils.hasText(text)) {
			text = text.trim();
			if(text.startsWith("%")) {
				return text;
			}
			return "%"+text;
		}
		return "%";
	}
	
	public static String toStartWithLikeString(String text) {
		if(StringUtils.hasText(text)) {
			text = text.trim();
			if(text.endsWith("%")) {
				return text;
			}
			return text + "%";
		}
		return "%";
	}
	
	public static String toContainsLikeString(String text) {
		return toStartWithLikeString(toEndWithLikeString(text));
	}
	
	
}
