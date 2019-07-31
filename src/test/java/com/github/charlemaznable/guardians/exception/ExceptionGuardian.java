package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

@Component
public class ExceptionGuardian {

    @Guard
    public boolean guard() {
        throw new GuardianException("ExceptionGuardian");
    }
}
