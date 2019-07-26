package com.github.charlemaznable.guardians.context;

import com.github.charlemaznable.guardians.Guard;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import org.springframework.stereotype.Component;

import static com.github.charlemaznable.lang.Condition.checkNull;

@Component
public class ContextRequestGuardian {

    @Guard
    public boolean guard(MutableHttpServletRequest request, GuardContext context) {
        request.setParameter("prefix", checkNull(context,
                () -> "Error", GuardContext::contextValue));
        return true;
    }
}
