package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface NumberRestrict {
	double min() default 0.0;
	double max() default 0.0;
	boolean zeroInclue() default false;
	String messageOnNoInvalide() default "";
}
