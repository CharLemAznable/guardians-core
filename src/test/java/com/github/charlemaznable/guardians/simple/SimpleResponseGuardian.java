package com.github.charlemaznable.guardians.simple;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletResponse;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@Component
public class SimpleResponseGuardian {

    @Guard
    public void guard(MutableHttpServletResponse response) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("suffix", "SimpleResponseGuardian");
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
