package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/simple")
public class SimpleController {

    @RequestMapping("/simple")
    public void simple(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian
    @PostGuardian
    @RequestMapping("/empty")
    public void empty(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(SimpleGuardian.class)
    @PostGuardian(SimpleGuardian.class)
    @RequestMapping("/guarding")
    public void guarding(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(ErrorGuardian.class)
    @PostGuardian(ErrorGuardian.class)
    @RequestMapping("/guardingError")
    public void guardingError(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(FalseGuardian.class)
    @PostGuardian(FalseGuardian.class)
    @RequestMapping("/guardingFalse")
    public void guardingFalse(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
