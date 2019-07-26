package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PostGuardians;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.PreGuardians;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

import static com.github.charlemaznable.lang.Clz.invokeQuietly;
import static com.github.charlemaznable.lang.Clz.isAssignable;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.github.charlemaznable.lang.Listt.newArrayList;
import static com.github.charlemaznable.spring.MutableHttpServletUtils.mutableRequest;
import static com.github.charlemaznable.spring.MutableHttpServletUtils.mutableResponse;
import static com.github.charlemaznable.spring.SpringContext.getBean;
import static org.apache.commons.lang3.reflect.MethodUtils.getMethodsListWithAnnotation;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Slf4j
@Component
public class GuardianInterceptor implements HandlerInterceptor {

    private Cache<HandlerGuardianCacheKey, List<PreGuardian>>
            preGuardiansAnnotationCache = CacheBuilder.newBuilder().build();
    private Cache<HandlerGuardianCacheKey, List<PostGuardian>>
            postGuardiansAnnotationCache = CacheBuilder.newBuilder().build();

    @SneakyThrows
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (!(handler instanceof HandlerMethod)) return true;
        val handlerMethod = (HandlerMethod) handler;

        val cacheKey = new HandlerGuardianCacheKey(handlerMethod);
        List<PreGuardian> preGuardians = preGuardiansAnnotationCache.get(
                cacheKey, () -> findGuardians(cacheKey, PreGuardian.class,
                        PreGuardians.class, PreGuardians::value));
        if (preGuardians.size() == 0) return true;

        val mutableRequest = mutableRequest(request);
        val mutableResponse = mutableResponse(response);
        for (val preGuardian : preGuardians) {
            val guardianType = preGuardian.value();
            val guardian = getBean(guardianType);
            if (null == guardian) {
                log.warn("Cannot find Guardian Bean of Type: {}", guardianType);
                continue;
            }

            val contextTypes = nullThen(preGuardian.context(), () -> new Class<?>[0]);
            val guardMethods = getMethodsListWithAnnotation(guardianType, Guard.class);
            for (val guardMethod : guardMethods) {
                if (Boolean.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod,
                        mutableRequest, mutableResponse, contextTypes, cacheKey);
                val result = invokeQuietly(guardian, guardMethod, parameters);
                if (!(Boolean) result) return false;
            }
        }

        return true;
    }

    @SneakyThrows
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {

        if (!(handler instanceof HandlerMethod)) return;
        val handlerMethod = (HandlerMethod) handler;

        val cacheKey = new HandlerGuardianCacheKey(handlerMethod);
        List<PostGuardian> postGuardians = postGuardiansAnnotationCache.get(
                cacheKey, () -> findGuardians(cacheKey, PostGuardian.class,
                        PostGuardians.class, PostGuardians::value));
        if (postGuardians.size() == 0) return;

        val mutableRequest = mutableRequest(request);
        val mutableResponse = mutableResponse(response);
        for (val postGuardian : postGuardians) {
            val guardianType = postGuardian.value();
            val guardian = getBean(guardianType);
            if (null == guardian) {
                log.warn("Cannot find Guardian Bean of Type: {}", guardianType);
                continue;
            }

            val contextTypes = nullThen(postGuardian.context(), () -> new Class<?>[0]);
            val guardMethods = getMethodsListWithAnnotation(guardianType, Guard.class);
            for (val guardMethod : guardMethods) {
                if (Void.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod,
                        mutableRequest, mutableResponse, contextTypes, cacheKey);
                invokeQuietly(guardian, guardMethod, parameters);
            }
        }
    }

    private <Guardian extends Annotation, Guardians extends Annotation>
    List<Guardian> findGuardians(HandlerGuardianCacheKey cacheKey,
                                 Class<Guardian> guardianType,
                                 Class<Guardians> guardiansType,
                                 Function<Guardians, Guardian[]> fetcher) {

        val methodGuardians = findAnnotation(cacheKey.getMethod(), guardiansType);
        if (null != methodGuardians) return newArrayList(fetcher.apply(methodGuardians));
        val methodGuardian = findAnnotation(cacheKey.getMethod(), guardianType);
        if (null != methodGuardian) return newArrayList(methodGuardian);

        val classGuardians = findAnnotation(cacheKey.getDeclaringClass(), guardiansType);
        if (null != classGuardians) return newArrayList(fetcher.apply(classGuardians));
        val classGuardian = findAnnotation(cacheKey.getDeclaringClass(), guardianType);
        if (null != classGuardian) return newArrayList(classGuardian);

        return newArrayList();
    }

    @SuppressWarnings("unchecked")
    private Object[] buildGuardParameters(Method guardMethod,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Class<?>[] contextTypes,
                                          HandlerGuardianCacheKey cacheKey) {

        val parameterTypes = guardMethod.getParameterTypes();
        val parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            val parameterType = parameterTypes[i];
            if (isAssignable(parameterType, HttpServletRequest.class)) {
                parameters[i] = request;
            } else if (isAssignable(parameterType, HttpServletResponse.class)) {
                parameters[i] = response;
            } else if (isAssignable(parameterType, Annotation.class)) {
                Class<Annotation> annotationType = (Class<Annotation>) parameterType;
                val methodAnnotation = findAnnotation(cacheKey.getMethod(), annotationType);
                if (null != methodAnnotation) { parameters[i] = methodAnnotation; continue; }
                val classAnnotation = findAnnotation(cacheKey.getDeclaringClass(), annotationType);
                if (null != classAnnotation) { parameters[i] = classAnnotation; continue; }
                parameters[i] = null;
            } else {
                parameters[i] = null;
                for (val contextType : contextTypes) {
                    if (isAssignable(contextType, parameterType)) {
                        parameters[i] = getBean(contextType);
                        if (null == parameters[i]) {
                            log.warn("Cannot find Guardian Context Bean of Type: {}", contextType);
                        }
                        break;
                    }
                }
            }
        }
        return parameters;
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerGuardianCacheKey {

        private Method method;
        private Class<?> declaringClass;

        HandlerGuardianCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }
}
