package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.net.Http.fetchPathVariableMap;

@Getter
@AllArgsConstructor
public final class RequestPathVariableExtractor implements RequestValueExtractor {

    private String keyName;

    @Override
    public String extract(HttpServletRequest request) {
        return getStr(fetchPathVariableMap(request), keyName);
    }
}
