package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.net.Http.dealRequestBodyStream;

@Getter
@AllArgsConstructor
public class RequestBodyRawExtractFunction implements RequestValueExtractFunction {

    private String charsetName;

    @Override
    public String apply(HttpServletRequest request) {
        return dealRequestBodyStream(request, charsetName);
    }
}
