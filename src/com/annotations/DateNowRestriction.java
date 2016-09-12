package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DateNowRestriction {
	DATE_NOW_RESTRICTION value() default DATE_NOW_RESTRICTION.ANY;
}
