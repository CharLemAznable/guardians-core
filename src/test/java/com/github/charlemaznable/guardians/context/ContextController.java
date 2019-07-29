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

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextAlpha.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextAlpha.class})
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextBeta.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextBeta.class})
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextError.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextError.class})
    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
