package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.net.Http;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class RequestBodyRawExtractor implements Function<HttpServletRequest, String> {

    private String charsetName;

    @Override
    public String apply(HttpServletRequest request) {
        return Http.dealRequestBodyStream(request, charsetName);
    }
}
