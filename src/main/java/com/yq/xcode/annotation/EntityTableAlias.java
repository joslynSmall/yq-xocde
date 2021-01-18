package com.yq.xcode.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * l
 * @author jettie
 *
 */  
@Target(TYPE)
@Retention(RUNTIME)
public @interface EntityTableAlias {
	/**
	 * (Optional) The name of the table.
	 * <p/>
	 * Defaults to the entity name.
	 */
	String alias() default "";
	
	String workFlowCategoryCode () default "";
}

