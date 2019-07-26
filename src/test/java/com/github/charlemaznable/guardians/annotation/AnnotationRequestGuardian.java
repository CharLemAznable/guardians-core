package com.github.charlemaznable.guardians.annotation;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.lang.Condition.checkNull;

@Component
public class AnnotationRequestGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request, GuardAnno anno) {
        request.setParameter("prefix", checkNull(anno,
                () -> "Empty", GuardAnno::value));
        return true;
    }
}
