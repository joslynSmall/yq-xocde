package com.yq.xcode.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 视图层vo返回参数映射
 * @author Joslyn
 *
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface EntityColumnTableAlias {

	/**
	 * 表别名
	 * @return
	 */
	String tableAlias() default "";

	/**
	 * 字段数据库名,不填写默认字段驼峰转下划线
	 * @return
	 */
	String columnAliasc() default "";

}

