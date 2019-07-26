package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreGuardian(AnnotationRequestGuardian.class)
@PostGuardian(AnnotationResponseGuardian.class)
public @interface AnnotationGuardian {
}
