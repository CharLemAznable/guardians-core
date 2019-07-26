package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/annotation")
@PreGuardian(AnnotationGuardian.class)
@PostGuardian(AnnotationGuardian.class)
public class AnnotationErrorController {

    @RequestMapping("/error")
    public void error(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
