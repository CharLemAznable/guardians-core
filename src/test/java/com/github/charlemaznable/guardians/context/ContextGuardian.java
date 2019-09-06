package com.github.charlemaznable.guardians.context;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import com.github.charlemaznable.core.spring.MutableHttpServletResponse;
import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkNull;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;

@Component
public class ContextGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request, GuardContext context) {
        request.setParameter("prefix", checkNull(context,
                () -> "Error", GuardContext::contextValue));
        return true;
    }

    @Guard
    public void guard(MutableHttpServletResponse response, GuardContext context) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("suffix", checkNull(context,
                    () -> "Error", GuardContext::contextValue));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
