package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

@Getter
@AllArgsConstructor
public final class RequestHeaderExtractor implements RequestValueExtractor {

    private String keyName;

    @Override
    public String extract(HttpServletRequest request) {
        return request.getHeader(keyName);
    }
}
