package com.yq.xcode.common.audit.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD,METHOD}) 
@Retention(RUNTIME)
public @interface Auditable {

	public AuditableProperty[] properties() default {} ;
	public boolean hidden() default false;
	public String name() default "";
}
