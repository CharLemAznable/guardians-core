package com.github.charlemaznable.guardians.threadlocal;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.guardians.spring.GuardianContext;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Condition.notNullThen;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@Component
public class PostGammaGuardian {

    @Guard
    public void guard() {
        MutableHttpServletUtils.mutateResponse(GuardianContext.response(), mutableResponse -> {
            if (null == GuardianContext.get("context")) return;

            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.putAll(GuardianContext.all());
            contentMap.put("method", notNullThen(GuardianContext.handlerMethod(), Method::getName));
            contentMap.put("class", notNullThen(GuardianContext.handlerDeclaringClass(), Class::getSimpleName));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
