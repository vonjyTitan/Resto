package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DateRestrict {
	String after() default "";
	String before() default "";
	String messageOnNoInvalide() default "";
}
