package com.yq.xcode.web.ui.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ColumnLable {
	/**
	 * 中文名称 
	 * @return
	 */
	String name() default "";
 	
	/**
	 * 是否掩藏字段
	 */
	boolean hidden() default false;
	
	int lineNum() default 1 ;
	
	/**
	 * 描述, 将来为导出到excel 用，
	 * @return
	 */
	String desc() default "";

	/**
	 * 选择数据的类型， 这样查询 显示 编辑就可以共用了
	 * @return
	 */
	String listCategory() default "";
	
	/**
	 * 控件类型， 
	 * @return
	 */
	String tagKey() default "";
	
	/**
	 * DB 字符长度 
	 * @return
	 */
	int colLength() default 0;
	
	
	boolean mandatory() default false;
	
	/**
	 * 不可以页面定义，true  在页面定义时， 将读不到 
	 */
	boolean canNotDefine() default false;
	
	/**
	 * 扩展字段
	 */
	boolean extProperty() default false;
}
