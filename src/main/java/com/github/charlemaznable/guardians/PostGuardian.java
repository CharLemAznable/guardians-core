package com.github.charlemaznable.guardians;

import com.github.charlemaznable.guardians.spring.DefaultGuardian;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PostGuardians.class)
public @interface PostGuardian {

    @AliasFor("type")
    Class<?> value() default DefaultGuardian.class;

    @AliasFor("value")
    Class<?> type() default DefaultGuardian.class;

    Class<?>[] context() default {};
}
