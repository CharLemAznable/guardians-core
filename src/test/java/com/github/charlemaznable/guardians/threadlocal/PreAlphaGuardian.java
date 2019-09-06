package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
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
