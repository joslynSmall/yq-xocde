package com.yq.xcode.common.audit.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(FIELD) 
@Retention(RUNTIME)
public @interface AuditableProperty {

	public String value() ;
}
