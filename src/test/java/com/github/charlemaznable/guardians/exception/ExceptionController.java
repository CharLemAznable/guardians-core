package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.net.Http.responseText;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

    @PreGuardian(ExceptionTrueGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/true")
    public void pretrue(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionFalseGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/false")
    public void prefalse(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/exception")
    public void exception(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionGuardian.class)
    @PostGuardian(ExceptionPostUnhandledGuardian.class)
    @RequestMapping("/exceptionUnhandled")
    public void exceptionUnhandled(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionRuntimeGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/runtime")
    public void runtime(HttpServletRequest request, HttpServletResponse response) {
        responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionTrueGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/runtimeBiz")
    public void runtimeBiz(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException("BusinessRuntimeException");
    }

    @ExceptionHandler(RuntimeException.class)
    public void handlerRuntimeException(RuntimeException exception, HttpServletResponse response) {
        responseText(response, "ExceptionHandler: " + exception.getMessage());
    }
}
