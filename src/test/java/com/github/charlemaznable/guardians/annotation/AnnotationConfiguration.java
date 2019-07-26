package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.spring.GuardianImport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@GuardianImport
public class AnnotationConfiguration {
}
