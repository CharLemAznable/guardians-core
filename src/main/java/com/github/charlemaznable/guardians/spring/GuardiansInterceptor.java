package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.NoneGuardian;
import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PostGuardians;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.PreGuardians;
import com.github.charlemaznable.guardians.exception.GuardianException;
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
import java.util.Optional;
import java.util.function.Function;

import static com.github.charlemaznable.lang.Clz.invokeQuietly;
import static com.github.charlemaznable.lang.Clz.isAssignable;
import static com.github.charlemaznable.lang.Condition.checkNull;
import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.github.charlemaznable.lang.Listt.newArrayList;
import static com.github.charlemaznable.spring.MutableHttpServletUtils.mutableRequest;
import static com.github.charlemaznable.spring.MutableHttpServletUtils.mutableResponse;
import static com.github.charlemaznable.spring.SpringContext.getBean;
import static org.apache.commons.lang3.reflect.MethodUtils.getMethodsListWithAnnotation;
import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;

@Slf4j
@Component
public class GuardiansInterceptor implements HandlerInterceptor {

    private Cache<HandlerGuardiansCacheKey, Optional<NoneGuardian>>
            noneGuardianAnnotationCache = CacheBuilder.newBuilder().build();
    private Cache<HandlerGuardiansCacheKey, List<PreGuardian>>
            preGuardiansAnnotationCache = CacheBuilder.newBuilder().build();
    private Cache<HandlerGuardiansCacheKey, List<PostGuardian>>
            postGuardiansAnnotationCache = CacheBuilder.newBuilder().build();
    private Cache<Class<?>, List<Method>>
            guardianMethodListCache = CacheBuilder.newBuilder().build();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        try {
            GuardianContext.setup(request, response);
            return preHandleInternal(request, response, handler);
        } catch (GuardianReturnFalse | GuardianException ex) {
            val e = ex instanceof GuardianReturnFalse ? null : ex;
            afterCompletionInternal(request, response, handler, e);
            GuardianContext.teardown();
            return false;
        } catch (Exception ex) {
            GuardianContext.teardown();
            throw ex;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex) {
        try {
            afterCompletionInternal(request, response, handler, ex);
        } finally {
            GuardianContext.teardown();
        }
    }

    @SneakyThrows
    public boolean preHandleInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Object handler) {

        if (!(handler instanceof HandlerMethod)) return true;
        val handlerMethod = (HandlerMethod) handler;
        val cacheKey = new HandlerGuardiansCacheKey(handlerMethod);

        val noneGuardian = noneGuardianAnnotationCache.get(cacheKey,
                () -> findNoneGuardian(cacheKey));
        if (noneGuardian.isPresent()) return true;

        val preGuardians = preGuardiansAnnotationCache.get(cacheKey,
                () -> findGuardians(cacheKey, PreGuardian.class,
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
            val guardMethods = guardianMethodListCache.get(guardianType,
                    () -> findGuardMethodList(guardianType));
            for (val guardMethod : guardMethods) {
                if (Boolean.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod,
                        mutableRequest, mutableResponse, contextTypes, cacheKey, null);
                val result = invokeQuietly(guardian, guardMethod, parameters);
                if (!(result instanceof Boolean) || !(Boolean) result) {
                    throw new GuardianReturnFalse();
                }
            }
        }
        return true;
    }

    @SneakyThrows
    public void afterCompletionInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Exception ex) {

        if (!(handler instanceof HandlerMethod)) return;
        val handlerMethod = (HandlerMethod) handler;
        val cacheKey = new HandlerGuardiansCacheKey(handlerMethod);

        val noneGuardian = noneGuardianAnnotationCache.get(cacheKey,
                () -> findNoneGuardian(cacheKey));
        if (noneGuardian.isPresent()) return;

        val postGuardians = postGuardiansAnnotationCache.get(cacheKey,
                () -> findGuardians(cacheKey, PostGuardian.class,
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
            val guardMethods = guardianMethodListCache.get(guardianType,
                    () -> findGuardMethodList(guardianType));
            for (val guardMethod : guardMethods) {
                if (Void.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod,
                        mutableRequest, mutableResponse, contextTypes, cacheKey, ex);
                invokeQuietly(guardian, guardMethod, parameters);
            }
        }
    }

    private Optional<NoneGuardian> findNoneGuardian(HandlerGuardiansCacheKey cacheKey) {
        val methodNoneGuardian = findMergedAnnotation(cacheKey.getMethod(), NoneGuardian.class);
        if (null != methodNoneGuardian) return Optional.of(methodNoneGuardian);

        val classNoneGuardian = findMergedAnnotation(cacheKey.getDeclaringClass(), NoneGuardian.class);
        if (null != classNoneGuardian) return Optional.of(classNoneGuardian);

        return Optional.empty();
    }

    private <Guardian extends Annotation, Guardians extends Annotation>
    List<Guardian> findGuardians(HandlerGuardiansCacheKey cacheKey,
                                 Class<Guardian> guardianType,
                                 Class<Guardians> guardiansType,
                                 Function<Guardians, Guardian[]> fetcher) {

        val methodGuardians = findMergedAnnotation(cacheKey.getMethod(), guardiansType);
        if (null != methodGuardians) return newArrayList(fetcher.apply(methodGuardians));
        val methodGuardian = findMergedAnnotation(cacheKey.getMethod(), guardianType);
        if (null != methodGuardian) return newArrayList(methodGuardian);

        val classGuardians = findMergedAnnotation(cacheKey.getDeclaringClass(), guardiansType);
        if (null != classGuardians) return newArrayList(fetcher.apply(classGuardians));
        val classGuardian = findMergedAnnotation(cacheKey.getDeclaringClass(), guardianType);
        if (null != classGuardian) return newArrayList(classGuardian);

        return newArrayList();
    }

    List<Method> findGuardMethodList(Class<?> guardianType) {
        List<Method> guardMethodList = newArrayList();
        val allMethods = getMethodsListWithAnnotation(guardianType, Guard.class);
        for (val annotatedMethod : allMethods) {
            if (guardianType == annotatedMethod.getDeclaringClass() ||
                    checkNull(findMergedAnnotation(annotatedMethod,
                            Guard.class), () -> false, Guard::inherited)) {
                guardMethodList.add(annotatedMethod);
            }
        }
        return guardMethodList;
    }

    @SuppressWarnings("unchecked")
    private Object[] buildGuardParameters(Method guardMethod,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Class<?>[] contextTypes,
                                          HandlerGuardiansCacheKey cacheKey,
                                          Exception exception) {

        val parameterTypes = guardMethod.getParameterTypes();
        val parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            val parameterType = parameterTypes[i];
            if (isAssignable(parameterType, HttpServletRequest.class)) {
                parameters[i] = request;
            } else if (isAssignable(parameterType, HttpServletResponse.class)) {
                parameters[i] = response;
            } else if (isAssignable(parameterType, Annotation.class)) {
                val annotationType = (Class<Annotation>) parameterType;
                parameters[i] = findMergedAnnotation(cacheKey.getMethod(), annotationType);
                if (null != parameters[i]) continue;
                parameters[i] = findMergedAnnotation(cacheKey.getDeclaringClass(), annotationType);
            } else if (isAssignable(parameterType, Exception.class)) {
                if (null == exception) continue;
                parameters[i] = isAssignable(exception.getClass(), parameterType) ? exception : null;
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
    static class HandlerGuardiansCacheKey {

        private Method method;
        private Class<?> declaringClass;

        HandlerGuardiansCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }

    static class GuardianReturnFalse extends RuntimeException {

        private static final long serialVersionUID = -1946637272350492943L;
    }
}
