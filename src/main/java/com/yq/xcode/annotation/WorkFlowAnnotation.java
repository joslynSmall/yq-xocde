package com.yq.xcode.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * l
 * @author jettie
 *
 */  
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WorkFlowAnnotation {
 
	String categoryCode() ;
	String categoryName() ;
	
	int lineNumber()  default 0 ;
    Class criteriaClass()   ;
    Class entityClass()   ;
  	String openUrl() ;
	String wxUrl()  default ""; 
	
	/**
	 * [
	     {"itemKey":"1","itemName":"userName"}, 属性mapping 到 segment1..10
	     {"itemKey":"2","itemName":"deptName"}
	   ] 
	 * @return
	 */
	String   attributeDef()  default "";
 
}

