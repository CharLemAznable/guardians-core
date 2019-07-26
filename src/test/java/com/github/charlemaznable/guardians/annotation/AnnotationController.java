package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/annotation")
@AnnotationGuardian
@GuardAnno("DEFAULT")
public class AnnotationController {

    @RequestMapping("/default")
    public void def(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @GuardAnno("ALPHA")
    @RequestMapping("/alpha")
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @GuardAnno("BETA")
    @RequestMapping("/beta")
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
