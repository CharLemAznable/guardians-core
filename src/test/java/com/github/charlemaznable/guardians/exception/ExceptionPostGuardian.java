package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.lang.Condition.checkNull;
import static com.github.charlemaznable.core.net.Http.responseText;

@Component
public class ExceptionPostGuardian {

    @Guard
    public void guard(HttpServletResponse response, GuardianException exception) {
        responseText(response, checkNull(exception, () -> "No exception",
                e -> "ExceptionPostGuardian: " + e.getMessage()));
    }
}
