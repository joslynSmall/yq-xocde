package com.yq.xcode.web.ui.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;




@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface CriteriaCol {
	/**
	 * 
	 * 标签名称
	 * @return
	 * 
	 */
	
	String tagKey() default "";
	/**
	 *  
	 * @return
	 */
	int lineNum() default 1;
	/**
	 * 数据库查询列表名称
	 * @return
	 */
	String colName() default "";
	
    /**
     * 显示标题
     */
	String lable() default "";

	/**
	 * 比较类型
	 * @return
	 */
	String operation() default "";
	
	/**
	 * 参数值的预留处理,例如前匹配可以写concat(DataTypeConstants.PLACE_HOLDER,'%')
	 */
	String placeHolder() default "";
	/**
	 * ="{getMonth(null,-1)}" 默认值
	 * @return
	 */
	
	String value() default "";
	
	/**
	 * ="{getMonth(null,-1)}" 默认值
	 * @return
	 */
	
	String toValue() default "";	
	
	/**
	 * 
	 * 取数类型, 如果输入这个默认就是jobdownlist
	 * @return
	 * 
	 */	
	String listCategory() default "";

 
}
