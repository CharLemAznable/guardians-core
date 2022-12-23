package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.jsonOf;
import static com.github.charlemaznable.core.net.Http.responseJson;

@Component
public class FalseGuardian {

    @Guard
    public boolean guard(HttpServletResponse response) {
        responseJson(response, jsonOf("prefix", "FalseGuardian"));
        return false;
    }
}
