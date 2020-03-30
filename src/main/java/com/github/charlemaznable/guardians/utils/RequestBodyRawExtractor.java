package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Mapp.of;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;

@Getter
@AllArgsConstructor
public final class RequestBodyRawExtractor implements RequestValueExtractor {

    public static final String RAW = "RAW";
    private String charsetName;

    @Override
    public Map<String, Object> extract(HttpServletRequest request) {
        return of(RAW, dealRequestBodyStream(request, charsetName));
    }

    @Override
    public Object extractValue(HttpServletRequest request) {
        return extract(request).get(RAW);
    }
}
