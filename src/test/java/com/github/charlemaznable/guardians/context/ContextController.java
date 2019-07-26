package com.github.charlemaznable.guardians.context;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/context")
public class ContextController {

    @PreGuardian(type = ContextRequestGuardian.class, context = GuardContextAlpha.class)
    @PostGuardian(type = ContextResponseGuardian.class, context = GuardContextAlpha.class)
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextRequestGuardian.class, context = GuardContextBeta.class)
    @PostGuardian(type = ContextResponseGuardian.class, context = GuardContextBeta.class)
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextRequestGuardian.class, context = GuardContextError.class)
    @PostGuardian(type = ContextResponseGuardian.class, context = GuardContextError.class)
    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
