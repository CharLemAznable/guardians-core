package com.github.charlemaznable.guardians.utils;

import lombok.Getter;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.blankThen;
import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.FORM;
import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
public final class RequestBodyFormatExtractor implements RequestValueExtractor {

    private List<String> keyNames;
    private RequestBodyFormat format;
    private String charsetName;

    public RequestBodyFormatExtractor(Iterable<String> keyNames) {
        this(keyNames, null, null);
    }

    public RequestBodyFormatExtractor(Iterable<String> keyNames, RequestBodyFormat format, String charsetName) {
        this.keyNames = newArrayList(keyNames);
        this.format = nullThen(format, () -> FORM);
        this.charsetName = blankThen(charsetName, UTF_8::name);
    }

    @Override
    public Map<String, Object> extract(HttpServletRequest request) {
        val requestBody = dealRequestBodyStream(request, charsetName);
        val requestBodyMap = format.parse(requestBody, charsetName);
        return requestBodyMap.entrySet().stream()
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
