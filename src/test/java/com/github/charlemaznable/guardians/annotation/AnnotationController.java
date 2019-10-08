package com.github.charlemaznable.guardians.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/annotation")
@GuardianRepeatableAnnotation
@GuardianAnnotation
public class AnnotationController {

    @RequestMapping("/default")
    public void def(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianRepeatableAnnotation("z")
    @GuardianParamAnnotation("ALPHA")
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianRepeatableAnnotation("z")
    @GuardianRepeatableAnnotation("y")
    @GuardianRepeatableAnnotation("x")
    @GuardianParamAnnotation("BETA")
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianRepeatableAnnotation("z")
    @GuardianAnnotation(value = "GAMMA")
    @RequestMapping("/gamma")
    public void gamma(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @GuardianRepeatableAnnotation("z")
    @GuardianRepeatableAnnotation("y")
    @GuardianRepeatableAnnotation("x")
    @GuardianAnnotation(value = "DELTA")
    @RequestMapping("/delta")
    public void delta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
