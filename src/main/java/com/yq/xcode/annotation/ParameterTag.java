package com.yq.xcode.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;




@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ParameterTag { 
	
	int lineNumber() default 1;
 	
	String endProperty() default "";
	
	String tagKey() default "";
	
	String lable() default "";
	
	String postLable() default "";
	
	String listCategory() default "";
	
	String defaultValue() default "";
	
	String placeHolder() default "";
	
	 

}
