package com.github.charlemaznable.guardians.inherited;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class ConcreteGuardian extends AbstractGuardian {

    @Guard
    public boolean method(MutableHttpServletRequest request) {
        request.setParameter("method", "ConcreteGuardian");
        return true;
    }

    // inherit method1 default

    @Guard
    public boolean method2(MutableHttpServletRequest request) {
        request.setParameter("method2", "ConcreteGuardian");
        return true;
    }

    public boolean method3(MutableHttpServletRequest request) {
        request.setParameter("method3", "ConcreteGuardian");
        return true;
    }
}
