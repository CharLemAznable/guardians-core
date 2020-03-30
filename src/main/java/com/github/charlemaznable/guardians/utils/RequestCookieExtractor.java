package com.github.charlemaznable.guardians.utils;

import lombok.Getter;
import lombok.val;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;

@Getter
public final class RequestCookieExtractor implements RequestValueExtractor {

    private List<String> keyNames;

    public RequestCookieExtractor(Iterable<String> keyNames) {
        this.keyNames = newArrayList(keyNames);
    }

    @Override
    public Map<String, Object> extract(HttpServletRequest request) {
        val cookies = nullThen(request.getCookies(), () -> new Cookie[]{});
        return newArrayList(cookies).stream()
                .filter(cookie -> keyNames.contains(cookie.getName()))
                .collect(HashMap::new, (m, c) ->
                        m.put(c.getName(), c.getValue()), HashMap::putAll);
    }

    @Override
    public Object extractValue(HttpServletRequest request) {
        if (1 == keyNames.size()) {
            return extract(request).get(keyNames.get(0));
        }
        return null;
    }
}
