package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.guardians.NoneGuardian;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/none")
@NoneGuardian
public class NoneController {

    @RequestMapping("/none")
    public void none(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
