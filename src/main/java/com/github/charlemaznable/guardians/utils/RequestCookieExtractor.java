package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

import static com.github.charlemaznable.lang.Condition.notNullThen;
import static com.github.charlemaznable.lang.Condition.nullThen;

@Getter
@Setter
@AllArgsConstructor
public class RequestCookieExtractor implements Function<HttpServletRequest, Cookie>, KeyedStringValueExtractor {

    private String cookieName;

    @Override
    public Cookie apply(HttpServletRequest request) {
        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(cookieName)) return cookie;
        }
        return null;
    }

    @Override
    public String extract(HttpServletRequest request) {
        return notNullThen(apply(request), Cookie::getValue);
    }
}
