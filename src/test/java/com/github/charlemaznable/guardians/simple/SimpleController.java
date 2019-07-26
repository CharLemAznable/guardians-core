package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/simple")
public class SimpleController {

    @RequestMapping("/simple")
    public void simple(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian
    @PostGuardian
    @RequestMapping("/empty")
    public void empty(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(SimpleRequestGuardian.class)
    @PostGuardian(SimpleResponseGuardian.class)
    @RequestMapping("/guarding")
    public void guarding(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(ErrorRequestGuardian.class)
    @PostGuardian(ErrorResponseGuardian.class)
    @RequestMapping("/guardingError")
    public void guardingError(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
