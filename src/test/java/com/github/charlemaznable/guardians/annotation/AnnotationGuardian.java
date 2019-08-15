package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import com.github.charlemaznable.spring.MutableHttpServletResponse;
import com.github.charlemaznable.spring.MutableHttpServletUtils;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.lang.Condition.checkEmpty;
import static com.github.charlemaznable.lang.Condition.checkNull;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

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
        MutableHttpServletUtils.mutateResponse(response, mutableResponse -> {
            val contentAsString = mutableResponse.getContentAsString();
            val contentMap = newHashMap(unJson(contentAsString));
            contentMap.put("suffix", checkNull(anno,
                    () -> "Empty", GuardianParamAnnotation::value));
            mutableResponse.setContentByString(json(contentMap));
        });
    }
}
