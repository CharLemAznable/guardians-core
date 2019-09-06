package com.github.charlemaznable.guardians.threadlocal;

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
@RequestMapping("/threadlocal")
@PostGuardian(PostGammaGuardian.class)
public class ThreadLocalController {

    @RequestMapping("/alpha")
    @PreGuardian(PreAlphaGuardian.class)
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }

    @RequestMapping("/beta")
    @PreGuardian(PreBetaGuardian.class)
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
