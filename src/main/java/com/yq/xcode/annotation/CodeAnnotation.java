package com.yq.xcode.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface CodeAnnotation {
	
	String prefix() ;

	String suffix() ;

	int numberSize() default 5;

	String sequenceId() ;

}
