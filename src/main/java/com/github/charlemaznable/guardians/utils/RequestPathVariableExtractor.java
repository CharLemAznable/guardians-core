package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.net.Http;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

import static com.github.charlemaznable.lang.Mapp.getStr;

@Getter
@Setter
@AllArgsConstructor
public class RequestPathVariableExtractor implements Function<HttpServletRequest, String>, RequestKeyedValueExtractor {

    private String keyName;

    @Override
    public String apply(HttpServletRequest request) {
        return getStr(Http.fetchPathVariableMap(request), keyName);
    }

    @Override
    public String extract(HttpServletRequest request) {
        return apply(request);
    }
}
