package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.lang.Condition.checkNull;
import static com.github.charlemaznable.net.Http.responseText;

@Component
public class ExceptionPostUnhandledGuardian {

    @Guard
    public void guard(HttpServletResponse response, GuardianTestException exception) {
        responseText(response, checkNull(exception, () -> "No exception",
                e -> "ExceptionPostUnhandledGuardian: " + e.getMessage()));
    }

    public static class GuardianTestException extends GuardianException {

        private static final long serialVersionUID = 7078357737545760722L;
    }
}
