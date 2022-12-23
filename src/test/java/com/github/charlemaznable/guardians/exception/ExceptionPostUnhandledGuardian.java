package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.Serial;

import static com.github.charlemaznable.core.lang.Condition.checkNull;
import static com.github.charlemaznable.core.net.Http.responseText;

@Component
public class ExceptionPostUnhandledGuardian {

    @Guard
    public void guard(HttpServletResponse response, GuardianTestException exception) {
        responseText(response, checkNull(exception, () -> "No exception",
                e -> "ExceptionPostUnhandledGuardian: " + e.getMessage()));
    }

    public static class GuardianTestException extends GuardianException {

        @Serial
        private static final long serialVersionUID = 7078357737545760722L;
    }
}
