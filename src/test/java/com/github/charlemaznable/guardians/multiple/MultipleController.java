package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.guardians.NoneGuardian;
import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.net.Http.fetchParameterMap;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Controller
@RequestMapping("/multiple")
@MultipleGuardian
public class MultipleController {

    @RequestMapping("/default")
    public void def(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @MultipleGuardian
    @RequestMapping("/compose")
    public void compose(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @PreGuardian(MultipleAlphaGuardian.class)
    @PreGuardian(MultipleBetaGuardian.class)
    @PostGuardian(MultipleBetaGuardian.class)
    @PostGuardian(MultipleAlphaGuardian.class)
    @RequestMapping("/plain")
    public void plain(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @NoneGuardian
    @RequestMapping("/none")
    public void none(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
