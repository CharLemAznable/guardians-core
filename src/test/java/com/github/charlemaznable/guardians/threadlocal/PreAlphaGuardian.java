package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class PreAlphaGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request) {
        GuardianContext.set("prefix", "PreAlphaGuardian");
        return true;
    }
}
