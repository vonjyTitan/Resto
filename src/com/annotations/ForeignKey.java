package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mapping.DataEntity;

@Retention(RetentionPolicy.RUNTIME)
public @interface ForeignKey {
	String totable() default "";
	Class toclasse() ;
	String pktable()  default "";
	String libtable() default "";
	boolean nullable() default false;
	SELECT_TYPE selecttype() default SELECT_TYPE.OPTION;
}
