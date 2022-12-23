package com.github.charlemaznable.guardians.context;

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
@RequestMapping("/context")
public class ContextController {

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextAlpha.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextAlpha.class})
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextBeta.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextBeta.class})
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextError.class})
    @PostGuardian(type = ContextGuardian.class, context = {GuardEmptyContext.class, GuardContextError.class})
    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
