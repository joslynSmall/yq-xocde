package com.yq.xcode.web.ui.annotation;


import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target(TYPE)
@Retention(RUNTIME)
public @interface EntityDesc {
	/**
	 * 中文名称 
	 * @return
	 */
	String name() default "";

 
	
	/**
	 * 自动生成页面， 为将来的按包生成用
	 * @return
	 */
	boolean autoGenPage() default true;

 	/**
	 * 描述, 将来为导出到excel 用，
	 * @return
	 */
	String desc() default "";
	
	/**
	 * 编辑页面显示的列数
	 * @return
	 */
	int editCols() default 3;
	
	/**
	 * 自动genservice
	 * @return
	 */
	boolean genService() default true;
	
	/**
	 * 自动gensaction
	 * @return
	 */
	boolean genAction() default true;
	
	/**
	 * 自动genCriteria
	 * @return
	 */
	boolean genCriteria() default true;

}
