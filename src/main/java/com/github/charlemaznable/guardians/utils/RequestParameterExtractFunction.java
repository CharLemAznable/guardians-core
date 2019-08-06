package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
@AllArgsConstructor
public class RequestParameterExtractFunction implements RequestValueExtractFunction {

    private String keyName;

    @Override
    public String apply(HttpServletRequest request) {
        return request.getParameter(keyName);
    }
}