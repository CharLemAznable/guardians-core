package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

@Component
public class ExceptionRuntimeGuardian {

    @Guard
    public boolean guard() {
        throw new RuntimeException("ExceptionRuntimeGuardian");
    }
}
