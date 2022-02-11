package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.spring.MutableHttpServletElf.setRequestParameter;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.spring.GuardianContext.set;

@Component
public class PreBetaGuardian {

    @Guard
    public boolean guard() {
        setRequestParameter(request(), "prefix", "PreBetaGuardian");
        set("context", "PreBetaGuardian");
        return false;
    }
}
