package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class RequestParameterExtractor implements Function<HttpServletRequest, String>, RequestKeyedValueExtractor {

    private String keyName;

    @Override
    public String apply(HttpServletRequest request) {
        return request.getParameter(keyName);
    }

    @Override
    public String extract(HttpServletRequest request) {
        return apply(request);
    }
}
