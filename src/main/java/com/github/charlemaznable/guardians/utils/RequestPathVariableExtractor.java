package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.lang.Mapp.getStr;
import static com.github.charlemaznable.net.Http.fetchPathVariableMap;

@Getter
@AllArgsConstructor
public class RequestPathVariableExtractor implements RequestKeyedValueExtractor {

    private String keyName;

    @Override
    public String apply(HttpServletRequest request) {
        return getStr(fetchPathVariableMap(request), keyName);
    }
}
