package com.yq.xcode.web.ui.annotation;


import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;



@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Detail {
	/**
	 * 明细中保存主表的属性名称，必须输入
	 * @return
	 */
	String masterProperty() default "";


}
