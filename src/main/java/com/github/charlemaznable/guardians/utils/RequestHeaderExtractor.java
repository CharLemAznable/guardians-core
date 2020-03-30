package com.github.charlemaznable.guardians.utils;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;

@Getter
public final class RequestHeaderExtractor implements RequestValueExtractor {

    private List<String> keyNames;

    public RequestHeaderExtractor(Iterable<String> keyNames) {
        this.keyNames = newArrayList(keyNames);
    }

    @Override
    public Map<String, Object> extract(HttpServletRequest request) {
        Map<String, Object> result = newHashMap();
        for (String keyName : keyNames) {
            result.put(keyName, request.getHeader(keyName));
        }
        return result;
    }

    @Override
    public Object extractValue(HttpServletRequest request) {
        if (1 == keyNames.size()) {
            return extract(request).get(keyNames.get(0));
        }
        return null;
    }
}
