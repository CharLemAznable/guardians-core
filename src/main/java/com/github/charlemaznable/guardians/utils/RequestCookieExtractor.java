package com.github.charlemaznable.guardians.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static com.github.charlemaznable.lang.Condition.nullThen;

@Getter
@AllArgsConstructor
public class RequestCookieExtractor implements RequestValueExtractor {

    private String keyName;

    @Override
    public String extract(HttpServletRequest request) {
        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        for (val cookie : cookies) {
            if (cookie.getName().equals(keyName)) return cookie.getValue();
        }
        return null;
    }
}
