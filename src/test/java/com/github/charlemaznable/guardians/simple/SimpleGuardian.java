package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import com.github.charlemaznable.core.spring.MutableHttpServletResponse;
import com.github.charlemaznable.guardians.Guard;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletElf.mutateResponse;

@Component
public class SimpleGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request) {
        request.setParameter("prefix", "SimpleGuardian");
        return true;
    }

    @Guard
    public void guard(MutableHttpServletResponse response) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("suffix", "SimpleGuardian");
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
