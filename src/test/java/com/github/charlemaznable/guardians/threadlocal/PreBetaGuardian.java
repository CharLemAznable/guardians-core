package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
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
