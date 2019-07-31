package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

@Component
public class ExceptionFalseGuardian {

    @Guard
    public boolean guard() {
        return false;
    }
}
