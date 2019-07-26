package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.spring.ComplexImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SuppressWarnings("SpringFacetCodeInspection")
@ComponentScan
@Configuration
@ComplexImport
public class GuardiansConfiguration implements WebMvcConfigurer {

    @Autowired
    private GuardiansInterceptor guardiansInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(guardiansInterceptor).addPathPatterns("/**");
    }
}
