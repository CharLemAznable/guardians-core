package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class SimpleRequestGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request) {
        request.setParameter("prefix", "SimpleRequestGuardian");
        return true;
    }
}
