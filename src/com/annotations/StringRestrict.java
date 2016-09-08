package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface StringRestrict {
	int minLength() default 0;
	int maxLength() default 0;
	boolean isAcceptCaracterSpec() default true;
	String messageOnNoInvalide() default "";
	String regex() default "";
}
