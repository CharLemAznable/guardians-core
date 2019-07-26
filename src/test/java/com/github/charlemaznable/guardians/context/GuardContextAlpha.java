package com.github.charlemaznable.guardians.context;

import org.springframework.stereotype.Component;

@Component
public class GuardContextAlpha implements GuardContext {

    @Override
    public String contextValue() {
        return "GuardContextAlpha";
    }
}
