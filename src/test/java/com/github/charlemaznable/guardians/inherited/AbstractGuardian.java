package com.github.charlemaznable.guardians.inherited;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.core.spring.MutableHttpServletRequest;

public abstract class AbstractGuardian {

    @Guard
    public boolean method0(MutableHttpServletRequest request) {
        request.setParameter("method0", "AbstractGuardian");
        return true;
    }

    @Guard(true)
    public boolean method1(MutableHttpServletRequest request) {
        request.setParameter("method1", "AbstractGuardian");
        return true;
    }

    @Guard(true)
    public boolean method2(MutableHttpServletRequest request) {
        request.setParameter("method2", "AbstractGuardian");
        return true;
    }

    @Guard(true)
    public boolean method3(MutableHttpServletRequest request) {
        request.setParameter("method3", "AbstractGuardian");
        return true;
    }
}
