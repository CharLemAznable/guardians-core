package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
public class RequestHeaderExtractor implements Function<HttpServletRequest, String> {

    private String headerName;

    @Override
    public String apply(HttpServletRequest request) {
        return request.getHeader(headerName);
    }
}
