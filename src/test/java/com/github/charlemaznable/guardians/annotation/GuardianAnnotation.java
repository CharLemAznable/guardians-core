package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreGuardian(AnnotationGuardian.class)
@PostGuardian(AnnotationGuardian.class)
@GuardianParamAnnotation
@GuardianRepeatableAnnotations
public @interface GuardianAnnotation {

    @AliasFor(annotation = GuardianParamAnnotation.class, attribute = "value")
    String value() default "DEFAULT";

    @AliasFor(annotation = GuardianRepeatableAnnotations.class, attribute = "value")
    GuardianRepeatableAnnotation[] repeat() default {};
}
