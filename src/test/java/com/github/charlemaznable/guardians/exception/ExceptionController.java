package com.github.charlemaznable.guardians.exception;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/exception")
public class ExceptionController {

    @PreGuardian(ExceptionTrueGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/true")
    public void pretrue(HttpServletRequest request, HttpServletResponse response) {
        Http.responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionFalseGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/false")
    public void prefalse(HttpServletRequest request, HttpServletResponse response) {
        Http.responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/exception")
    public void exception(HttpServletRequest request, HttpServletResponse response) {
        Http.responseText(response, "SUCCESS");
    }

    @PreGuardian(ExceptionRuntimeGuardian.class)
    @PostGuardian(ExceptionPostGuardian.class)
    @RequestMapping("/runtime")
    public void runtime(HttpServletRequest request, HttpServletResponse response) {
        Http.responseText(response, "SUCCESS");
    }

    @ExceptionHandler(RuntimeException.class)
    public void handlerRuntimeException(RuntimeException exception, HttpServletResponse response) {
        Http.responseText(response, "ExceptionHandler: " + exception.getMessage());
    }
}
