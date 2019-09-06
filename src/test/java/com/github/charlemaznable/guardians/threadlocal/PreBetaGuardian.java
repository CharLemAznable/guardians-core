package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import org.springframework.stereotype.Component;

@Component
public class PreBetaGuardian {

    @Guard
    public boolean guard() {
        MutableHttpServletUtils.setRequestParameter(
                GuardianContext.request(), "prefix", "PreBetaGuardian");
        GuardianContext.set("context", "PreBetaGuardian");
        return false;
    }
}
