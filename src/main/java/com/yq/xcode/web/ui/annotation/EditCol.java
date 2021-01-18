package com.yq.xcode.web.ui.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;




@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface EditCol {
	
	/**
	 *  
	 * @return
	 */ 
	int lineNum() default 1;
	
     /**
     * 显示标题
     */
	String lable() default "";

 	
	/**
	 * 
	 * 标签名称
	 * @return
	 * 
	 */	
	String tagKey() default "";
	
	/**
	 * 
	 * 取数类型, 如果输入这个默认就是jobdownlist
	 * @return
	 * 
	 */	
	String listCategory() default "";
	
	/**
	 * 是否必须输入
	 * @return
	 */
	boolean mandatory() default false;
	
	/**
	 * 只是显示，值是自动产生的
	 * @return
	 */
	boolean disabled() default false; 
	/**
	 * 附件显示方式, 是否显示图
	 * @return
	 */
	boolean showpic() default false;
	
	/**
	 * 提示信息, 附件
	 * @return
	 */
	String attachMsg() default "";
 
}
