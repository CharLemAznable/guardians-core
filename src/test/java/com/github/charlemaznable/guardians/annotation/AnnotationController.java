package com.github.charlemaznable.guardians.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/annotation")
@GuardianAnnotation
public class AnnotationController {

    @RequestMapping("/default")
    public void def(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianParamAnnotation("ALPHA")
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianParamAnnotation("BETA")
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianAnnotation("GAMMA")
    @RequestMapping("/gamma")
    public void gamma(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianAnnotation("DELTA")
    @RequestMapping("/delta")
    public void delta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
