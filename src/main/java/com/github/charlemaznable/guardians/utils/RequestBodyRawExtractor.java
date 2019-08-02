package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.net.Http.dealRequestBodyStream;

@Getter
@Setter
@AllArgsConstructor
public class RequestBodyRawExtractor implements RequestValueExtractor {

    private String charsetName;

    @Override
    public String apply(HttpServletRequest request) {
        return dealRequestBodyStream(request, charsetName);
    }
}
