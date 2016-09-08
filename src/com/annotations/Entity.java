package com.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.xml.ws.BindingType;

import org.omg.CosNaming.BindingTypeHelper;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity{
	String reference() default "";
	String pkName() default "id";
	String dataBasekey() default "";
}
