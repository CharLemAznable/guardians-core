package com.github.charlemaznable.guardians.spring;

import lombok.experimental.UtilityClass;
import lombok.val;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import static com.github.charlemaznable.lang.Condition.notNullThen;
import static com.github.charlemaznable.lang.Mapp.newHashMap;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@UtilityClass
public class GuardianContext {

    private ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<>();
    private ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<>();
    private ThreadLocal<Object> handlerContext = new ThreadLocal<>();
    private ThreadLocal<Map<String, Object>> customContext = new ThreadLocal<>();

    public void setup(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestContext.set(request);
        responseContext.set(response);
        handlerContext.set(handler);
        customContext.set(newHashMap());
    }

    public void teardown() {
        requestContext.set(null);
        responseContext.set(null);
        handlerContext.set(null);
        customContext.set(null);
    }

    public HttpServletRequest request() {
        return requestContext.get();
    }

    public HttpServletResponse response() {
        return responseContext.get();
    }

    public Object handler() {
        return handlerContext.get();
    }

    public Method handlerMethod() {
        val handler = handler();
        return handler instanceof HandlerMethod ?
                ((HandlerMethod) handler).getMethod() : null;
    }

    public Class<?> handlerDeclaringClass() {
        val handlerMethod = handlerMethod();
        return null != handlerMethod ?
                handlerMethod.getDeclaringClass() : null;
    }

    public <A extends Annotation> A handlerAnnotation(Class<A> annotationType) {
        val handler = handler();
        if (!(handler instanceof HandlerMethod)) return null;
        val methodHandler = (HandlerMethod) handler;
        val handlerMethod = methodHandler.getMethod();
        val handlerDeclaringClass = handlerMethod.getDeclaringClass();

        val annotation = findMergedAnnotation(handlerMethod, annotationType);
        if (null != annotation) return annotation;
        return findMergedAnnotation(handlerDeclaringClass, annotationType);
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
