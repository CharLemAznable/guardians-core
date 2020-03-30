package com.github.charlemaznable.guardians.utils;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.net.Http.fetchPathVariableMap;

@Getter
public final class RequestPathVariableExtractor implements RequestValueExtractor {

    private List<String> keyNames;

    public RequestPathVariableExtractor(Iterable<String> keyNames) {
        this.keyNames = newArrayList(keyNames);
    }

    @Override
    public Map<String, Object> extract(HttpServletRequest request) {
        return fetchPathVariableMap(request).entrySet().stream()
                .filter(e -> keyNames.contains(e.getKey()))
                .collect(HashMap::new, (m, e) ->
                        m.put(e.getKey(), e.getValue()), HashMap::putAll);
    }

    @Override
    public Object extractValue(HttpServletRequest request) {
        if (1 == keyNames.size()) {
            return extract(request).get(keyNames.get(0));
        }
        return null;
    }
}
