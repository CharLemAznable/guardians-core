package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.spring.GuardiansImport;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ComponentScan
@GuardiansImport
public class ThreadLocalConfiguration {
}
