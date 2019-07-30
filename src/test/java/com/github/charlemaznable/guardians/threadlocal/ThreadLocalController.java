package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/threadlocal")
@PostGuardian(PostGammaGuardian.class)
public class ThreadLocalController {

    @RequestMapping("/alpha")
    @PreGuardian(PreAlphaGuardian.class)
    public void alpha(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }

    @RequestMapping("/beta")
    @PreGuardian(PreBetaGuardian.class)
    public void beta(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
