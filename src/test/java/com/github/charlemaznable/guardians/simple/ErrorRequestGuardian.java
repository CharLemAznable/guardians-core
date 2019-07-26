package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletRequest;

public class ErrorRequestGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request) {
        request.setParameter("prefix", "ErrorRequestGuardian");
        return true;
    }
}
