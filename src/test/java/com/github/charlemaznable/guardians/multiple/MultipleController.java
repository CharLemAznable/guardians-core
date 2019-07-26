package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/multiple")
public class MultipleController {

    @PreGuardian(MultipleAlphaGuardian.class)
    @PreGuardian(MultipleBetaGuardian.class)
    @PostGuardian(MultipleBetaGuardian.class)
    @PostGuardian(MultipleAlphaGuardian.class)
    @RequestMapping("/plain")
    public void plain(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @MultipleGuardian
    @RequestMapping("/compose")
    public void compose(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
