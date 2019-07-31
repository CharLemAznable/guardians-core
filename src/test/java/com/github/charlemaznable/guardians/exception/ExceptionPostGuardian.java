package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.lang.Condition.checkNull;

@Component
public class ExceptionPostGuardian {

    @Guard
    public void guard(HttpServletResponse response, GuardianException exception) {
        Http.responseText(response, checkNull(exception, () -> "No exception",
                e -> "ExceptionPostGuardian: " + e.getMessage()));
    }
}
