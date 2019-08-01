package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import org.springframework.stereotype.Component;

@Component
public class PreAlphaGuardian {

    @Guard
    public boolean guard() {
        MutableHttpServletUtils.setRequestParameter(
                GuardianContext.request(), "prefix", "PreAlphaGuardian");
        GuardianContext.set("context", "PreAlphaGuardian");
        return true;
    }
}