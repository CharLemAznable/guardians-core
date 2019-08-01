package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Getter
@Setter
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
