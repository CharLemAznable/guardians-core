package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.net.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Getter
@Setter
@AllArgsConstructor
public class RequestBodyRawExtractor implements Function<HttpServletRequest, String> {

    private String charsetName;

    @Override
    public String apply(HttpServletRequest request) {
        return Http.dealRequestBodyStream(request, charsetName);
    }
}
