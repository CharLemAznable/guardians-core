package com.github.charlemaznable.guardians.spring;

import lombok.NoArgsConstructor;
import lombok.val;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static com.github.charlemaznable.core.lang.Condition.notNullThen;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.AnnotationElf.resolveContainerAnnotationType;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.getValue;

@NoArgsConstructor(access = PRIVATE)
public final class GuardianContext {

    private static ThreadLocal<HttpServletRequest> requestContext = new InheritableThreadLocal<>();
    private static ThreadLocal<HttpServletResponse> responseContext = new InheritableThreadLocal<>();
    private static ThreadLocal<Object> handlerContext = new InheritableThreadLocal<>();
    private static ThreadLocal<Map<String, Object>> customContext = new InheritableThreadLocal<>();

    public static void setup(HttpServletRequest request, HttpServletResponse response, Object handler) {
        requestContext.set(request);
        responseContext.set(response);
        handlerContext.set(handler);
        customContext.set(newHashMap());
    }

    public static void teardown() {
        requestContext.remove();
        responseContext.remove();
        handlerContext.remove();
        customContext.remove();
    }

    public static HttpServletRequest request() {
        return requestContext.get();
    }

    public static HttpServletResponse response() {
        return responseContext.get();
    }

    public static Object handler() {
        return handlerContext.get();
    }

    public static Method handlerMethod() {
        val handler = handler();
        return handler instanceof HandlerMethod ?
                ((HandlerMethod) handler).getMethod() : null;
    }

    public static Class<?> handlerDeclaringClass() {
        val handlerMethod = handlerMethod();
        return null != handlerMethod ?
                handlerMethod.getDeclaringClass() : null;
    }

    public static <A extends Annotation> A handlerAnnotation(Class<A> annotationType) {
        val handlerAnnotations = handlerAnnotations(annotationType);
        if (handlerAnnotations.isEmpty()) return null;
        return handlerAnnotations.get(0);
    }

    @SuppressWarnings("unchecked")
    public static <A extends Annotation> List<A> handlerAnnotations(Class<A> annotationType) {
        val handler = handler();
        if (!(handler instanceof HandlerMethod)) return newArrayList();
        HandlerMethod methodHandler = (HandlerMethod) handler;
        val handlerMethod = methodHandler.getMethod();
        val handlerDeclaringClass = handlerMethod.getDeclaringClass();

        val containerType = resolveContainerAnnotationType(annotationType);
        if (null == containerType) {
            val annotation = getMergedAnnotation(
                    handlerMethod, annotationType);
            if (null != annotation) return newArrayList(annotation);
            return newArrayList(getMergedAnnotation(
                    handlerDeclaringClass, annotationType));

        } else {
            val methodContainer = getMergedAnnotation(handlerMethod, containerType);
            if (null != methodContainer) return newArrayList((A[]) getValue(methodContainer));
            val methodAnnotation = getMergedAnnotation(handlerMethod, annotationType);
            if (null != methodAnnotation) return newArrayList(methodAnnotation);

            val classContainer = getMergedAnnotation(handlerDeclaringClass, containerType);
            if (null != classContainer) return newArrayList((A[]) getValue(classContainer));
            val classAnnotation = getMergedAnnotation(handlerDeclaringClass, annotationType);
            if (null != classAnnotation) return newArrayList(classAnnotation);

            return newArrayList();
        }
    }

    public static Map<String, Object> all() {
        return customContext.get();
    }

    public static Object get(String key) {
        return notNullThen(customContext.get(), m -> m.get(key));
    }

    public static void set(String key, Object value) {
        notNullThen(customContext.get(), m -> m.put(key, value));
    }
}
