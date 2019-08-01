package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.jsonOf;
import static com.github.charlemaznable.net.Http.responseJson;

@Component
public class FalseGuardian {

    @Guard
    public boolean guard(HttpServletResponse response) {
        responseJson(response, jsonOf("prefix", "FalseGuardian"));
        return false;
    }
}
