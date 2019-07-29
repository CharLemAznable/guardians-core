package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.guardians.NoneGuardian;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;

@Controller
@RequestMapping("/none")
@NoneGuardian
public class NoneController {

    @NoneGuardian
    @RequestMapping("/none")
    public void none(HttpServletRequest request, HttpServletResponse response) {
        Http.responseJson(response, json(Http.fetchParameterMap(request)));
    }
}
