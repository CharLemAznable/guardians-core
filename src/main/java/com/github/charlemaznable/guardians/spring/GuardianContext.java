package com.github.charlemaznable.guardians.spring;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.github.charlemaznable.lang.Condition.notNullThen;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@UtilityClass
public class GuardianContext {

    private ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<>();
    private ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<>();
    private ThreadLocal<Map<String, Object>> customContext = new ThreadLocal<>();

    public void setup(HttpServletRequest request, HttpServletResponse response) {
        requestContext.set(request);
        responseContext.set(response);
        customContext.set(newHashMap());
    }

    public void teardown() {
        requestContext.set(null);
        responseContext.set(null);
        customContext.set(null);
    }

    public HttpServletRequest request() {
        return requestContext.get();
    }

    public HttpServletResponse response() {
        return responseContext.get();
    }

    public Map<String, Object> all() {
        return customContext.get();
    }

    public Object get(String key) {
        return notNullThen(customContext.get(), m -> m.get(key));
    }

    public void set(String key, Object value) {
        notNullThen(customContext.get(), m -> m.put(key, value));
    }
}
