package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.net.Http.dealRequestBodyStream;

@Getter
@AllArgsConstructor
public class RequestBodyRawExtractor implements RequestValueExtractor {

    private String charsetName;

    @Override
    public String extract(HttpServletRequest request) {
        return dealRequestBodyStream(request, charsetName);
    }
}
