package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import com.github.charlemaznable.core.spring.MutableHttpServletResponse;
import com.github.charlemaznable.guardians.Guard;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.charlemaznable.core.codec.Json.json;
import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.lang.Condition.checkEmpty;
import static com.github.charlemaznable.core.lang.Condition.checkNull;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.spring.MutableHttpServletUtils.mutateResponse;

@Component
public class AnnotationGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request, GuardianParamAnnotation anno,
                         GuardianRepeatableAnnotation first, List<GuardianRepeatableAnnotation> annoList) {
        request.setParameter("prefix", checkNull(anno,
                () -> "Empty", GuardianParamAnnotation::value));
        request.setParameter("first", checkNull(first,
                () -> "Empty", GuardianRepeatableAnnotation::value));
        request.setParameter("list", checkEmpty(annoList,
                () -> "Empty", annotations -> {
                    val stringBuilder = new StringBuilder();
                    for (val annotation : annotations) {
                        stringBuilder.append(annotation.value());
                    }
                    return stringBuilder.toString();
                }));
        return true;
    }

    @Guard
    public void guard(MutableHttpServletResponse response, GuardianParamAnnotation anno) {
        mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("suffix", checkNull(anno,
                    () -> "Empty", GuardianParamAnnotation::value));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
