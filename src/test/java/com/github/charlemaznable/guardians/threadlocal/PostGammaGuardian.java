package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.notNullThen;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;
import static com.github.charlemaznable.guardians.spring.GuardianContext.all;
import static com.github.charlemaznable.guardians.spring.GuardianContext.get;
import static com.github.charlemaznable.guardians.spring.GuardianContext.handlerDeclaringClass;
import static com.github.charlemaznable.guardians.spring.GuardianContext.handlerMethod;
import static com.github.charlemaznable.guardians.spring.GuardianContext.response;

@Component
public class PostGammaGuardian {

    @Guard
    public void guard() {
        mutateResponse(response(), mutableResponse -> {
            if (null == get("context")) return;

            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.putAll(all());
            contentMap.put("method", notNullThen(handlerMethod(), Method::getName));
            contentMap.put("class", notNullThen(handlerDeclaringClass(), Class::getSimpleName));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
