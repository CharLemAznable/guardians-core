package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.net.Http;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.jsonOf;

@Component
public class FalseGuardian {

    @Guard
    public boolean guard(HttpServletResponse response) {
        Http.responseJson(response, jsonOf("prefix", "FalseGuardian"));
        return false;
    }
}
