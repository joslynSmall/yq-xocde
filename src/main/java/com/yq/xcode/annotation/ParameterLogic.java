package com.yq.xcode.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;




@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ParameterLogic {
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
	 * 作为参数
	 * @return
	 */

	boolean havingCase() default false;

}
