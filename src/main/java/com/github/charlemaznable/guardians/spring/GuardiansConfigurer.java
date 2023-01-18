package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.core.spring.ElvesImport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

@Configuration
@ElvesImport
public class GuardiansConfigurer implements WebMvcConfigurer {

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(guardiansInterceptor());
    }

    @Bean("com.github.charlemaznable.guardians.spring.GuardiansInterceptor")
    public GuardiansInterceptor guardiansInterceptor() {
        return new GuardiansInterceptor();
    }

    @Bean("com.github.charlemaznable.guardians.spring.DefaultGuardian")
    public DefaultGuardian defaultGuardian() {
        return new DefaultGuardian();
    }
}
