package com.github.charlemaznable.guardians.spring;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.NoneGuardian;
import com.github.charlemaznable.guardians.PostGuardian;
import com.github.charlemaznable.guardians.PreGuardian;
import com.github.charlemaznable.guardians.exception.GuardianException;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import static com.github.charlemaznable.core.lang.Clz.invokeQuietly;
import static com.github.charlemaznable.core.lang.Clz.isAssignable;
import static com.github.charlemaznable.core.lang.Condition.checkNull;
import static com.github.charlemaznable.core.lang.Listt.isNotEmpty;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutableRequest;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutableResponse;
import static com.github.charlemaznable.core.spring.SpringContext.getBean;
import static com.github.charlemaznable.guardians.spring.GuardianContext.handlerAnnotation;
import static com.github.charlemaznable.guardians.spring.GuardianContext.handlerAnnotations;
import static com.github.charlemaznable.guardians.spring.GuardianContext.request;
import static com.github.charlemaznable.guardians.spring.GuardianContext.response;
import static com.github.charlemaznable.guardians.spring.GuardianContext.setup;
import static com.github.charlemaznable.guardians.spring.GuardianContext.teardown;
import static org.apache.commons.lang3.reflect.MethodUtils.getMethodsListWithAnnotation;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedAnnotation;
import static org.springframework.core.annotation.AnnotatedElementUtils.getMergedRepeatableAnnotations;

@Slf4j
public final class GuardiansInterceptor implements HandlerInterceptor {

    private final Cache<HandlerGuardiansCacheKey, Optional<NoneGuardian>>
            noneGuardianAnnotationCache = CacheBuilder.newBuilder().build();
    private final Cache<HandlerGuardiansCacheKey, List<PreGuardian>>
            preGuardiansAnnotationCache = CacheBuilder.newBuilder().build();
    private final Cache<HandlerGuardiansCacheKey, List<PostGuardian>>
            postGuardiansAnnotationCache = CacheBuilder.newBuilder().build();
    private final Cache<Class<?>, List<Method>>
            guardianMethodListCache = CacheBuilder.newBuilder().build();

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request,
                             @Nonnull HttpServletResponse response,
                             @Nonnull Object handler) {
        try {
            val mutableRequest = mutableRequest(request);
            val mutableResponse = mutableResponse(response);
            setup(mutableRequest, mutableResponse, handler);

            preHandleInternal(request, response, handler);
            return true;
        } catch (GuardianReturnFalse e) {
            afterCompletionInternal(request, response, handler, null);
            teardown();
            return false;
        } catch (GuardianException e) {
            afterCompletionInternal(request, response, handler, e);
            teardown();
            return false;
        } catch (Exception ex) {
            teardown();
            throw ex;
        }
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request,
                                @Nonnull HttpServletResponse response,
                                @Nonnull Object handler, Exception ex) {
        try {
            afterCompletionInternal(request, response, handler, ex);
        } finally {
            teardown();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public void preHandleInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) return;
        val cacheKey = new HandlerGuardiansCacheKey(handlerMethod);

        val noneGuardian = noneGuardianAnnotationCache.get(cacheKey,
                () -> findNoneGuardian(cacheKey));
        if (noneGuardian.isPresent()) return;

        val preGuardians = preGuardiansAnnotationCache.get(cacheKey,
                () -> findGuardians(cacheKey, PreGuardian.class));
        if (preGuardians.size() == 0) return;

        for (val preGuardian : preGuardians) {
            val guardianType = preGuardian.value();
            val guardian = getBean(guardianType);
            if (null == guardian) {
                log.warn("Cannot find Guardian Bean of Type: {}", guardianType);
                continue;
            }

            val contextTypes = preGuardian.context();
            val guardMethods = guardianMethodListCache.get(guardianType,
                    () -> findGuardMethodList(guardianType));
            for (val guardMethod : guardMethods) {
                if (Boolean.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod, contextTypes, null);
                val result = invokeQuietly(guardian, guardMethod, parameters);
                if (!(result instanceof Boolean) || !(boolean) result) {
                    throw new GuardianReturnFalse();
                }
            }
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    public void afterCompletionInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler, Exception ex) {

        if (!(handler instanceof HandlerMethod handlerMethod)) return;
        val cacheKey = new HandlerGuardiansCacheKey(handlerMethod);

        val noneGuardian = noneGuardianAnnotationCache.get(cacheKey,
                () -> findNoneGuardian(cacheKey));
        if (noneGuardian.isPresent()) return;

        val postGuardians = postGuardiansAnnotationCache.get(cacheKey,
                () -> findGuardians(cacheKey, PostGuardian.class));
        if (postGuardians.size() == 0) return;

        for (val postGuardian : postGuardians) {
            val guardianType = postGuardian.value();
            val guardian = getBean(guardianType);
            if (null == guardian) {
                log.warn("Cannot find Guardian Bean of Type: {}", guardianType);
                continue;
            }

            val contextTypes = postGuardian.context();
            val guardMethods = guardianMethodListCache.get(guardianType,
                    () -> findGuardMethodList(guardianType));
            for (val guardMethod : guardMethods) {
                if (Void.TYPE != guardMethod.getReturnType()) continue;
                val parameters = buildGuardParameters(guardMethod, contextTypes, ex);
                invokeQuietly(guardian, guardMethod, parameters);
            }
        }
    }

    private Optional<NoneGuardian> findNoneGuardian(HandlerGuardiansCacheKey cacheKey) {
        val methodNoneGuardian = getMergedAnnotation(cacheKey.getMethod(), NoneGuardian.class);
        if (null != methodNoneGuardian) return Optional.of(methodNoneGuardian);

        val classNoneGuardian = getMergedAnnotation(cacheKey.getDeclaringClass(), NoneGuardian.class);
        if (null != classNoneGuardian) return Optional.of(classNoneGuardian);

        return Optional.empty();
    }

    private <G extends Annotation> List<G> findGuardians(HandlerGuardiansCacheKey cacheKey, Class<G> guardianType) {
        val methodGuardians = getMergedRepeatableAnnotations(cacheKey.getMethod(), guardianType);
        if (isNotEmpty(methodGuardians)) return newArrayList(methodGuardians);

        val classGuardians = getMergedRepeatableAnnotations(cacheKey.getDeclaringClass(), guardianType);
        if (isNotEmpty(classGuardians)) return newArrayList(classGuardians);

        return newArrayList();
    }

    List<Method> findGuardMethodList(Class<?> guardianType) {
        List<Method> guardMethodList = newArrayList();
        val allMethods = getMethodsListWithAnnotation(guardianType, Guard.class);
        for (val annotatedMethod : allMethods) {
            if (guardianType == annotatedMethod.getDeclaringClass() ||
                    checkNull(getMergedAnnotation(annotatedMethod,
                            Guard.class), () -> false, Guard::inherited)) {
                guardMethodList.add(annotatedMethod);
            }
        }
        return guardMethodList;
    }

    @SuppressWarnings("unchecked")
    private Object[] buildGuardParameters(Method guardMethod,
                                          Class<?>[] contextTypes,
                                          Exception exception) {

        val parameterTypes = guardMethod.getParameterTypes();
        val parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            val parameterType = parameterTypes[i];
            if (isAssignable(parameterType, HttpServletRequest.class)) {
                parameters[i] = request();
            } else if (isAssignable(parameterType, HttpServletResponse.class)) {
                parameters[i] = response();
            } else if (isAssignable(parameterType, Annotation.class)) {
                parameters[i] = handlerAnnotation((Class<Annotation>) parameterType);
            } else if (isAssignable(parameterType, List.class)) {
                val genericType = guardMethod.getGenericParameterTypes()[i];
                val componentType = (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
                if (isAssignable(componentType, Annotation.class)) {
                    parameters[i] = handlerAnnotations((Class<Annotation>) componentType);
                }
            } else if (isAssignable(parameterType, Exception.class)) {
                parameters[i] = buildParameterWithException(parameterType, exception);
            } else {
                parameters[i] = buildParameterWithContextTypes(parameterType, contextTypes);
            }
        }
        return parameters;
    }

    private Object buildParameterWithException(Class<?> parameterType, Exception exception) {
        if (null == exception) return null;
        return isAssignable(exception.getClass(), parameterType) ? exception : null;
    }

    private Object buildParameterWithContextTypes(Class<?> parameterType, Class<?>[] contextTypes) {
        for (val contextType : contextTypes) {
            if (isAssignable(contextType, parameterType)) {
                val parameter = getBean(contextType);
                if (null == parameter) {
                    log.warn("Cannot find Guardian Context Bean of Type: {}", contextType);
                }
                return parameter;
            }
        }
        return null;
    }

    @Getter
    @EqualsAndHashCode
    static class HandlerGuardiansCacheKey {

        private final Method method;
        private final Class<?> declaringClass;

        HandlerGuardiansCacheKey(HandlerMethod handlerMethod) {
            this.method = handlerMethod.getMethod();
            this.declaringClass = this.method.getDeclaringClass();
        }
    }

    static class GuardianReturnFalse extends RuntimeException {

        @Serial
        private static final long serialVersionUID = -1946637272350492943L;
    }
}
