package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

@Component
public class ExceptionTrueGuardian {

    @Guard
    public boolean guard() {
        return true;
    }
}
