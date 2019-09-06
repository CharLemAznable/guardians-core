package com.github.charlemaznable.guardians.multiple;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import com.github.charlemaznable.core.spring.MutableHttpServletResponse;
import com.github.charlemaznable.core.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.lang.Str.toStr;

@Component
public class MultipleBetaGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request) {
        val prefix = toStr(request.getParameter("prefix"));
        request.setParameter("prefix", prefix + "MultipleBetaGuardian");
        return true;
    }

    @Guard
    public void guard(MutableHttpServletResponse response) {
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            val suffix = toStr(contentMap.get("suffix"));
            contentMap.put("suffix", suffix + "MultipleBetaGuardian");
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
