package com.github.charlemaznable.guardians.spring;

import lombok.experimental.UtilityClass;

import java.util.Map;

import static com.github.charlemaznable.lang.Condition.notNullThen;
import static com.github.charlemaznable.lang.Mapp.newHashMap;

@UtilityClass
public class GuardianContext {

    private ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    public void setup() {
        context.set(newHashMap());
    }

    public void teardown() {
        context.set(null);
    }

    public Map<String, Object> all() {
        return context.get();
    }

    public Object get(String key) {
        return notNullThen(context.get(), m -> m.get(key));
    }

    public void set(String key, Object value) {
        notNullThen(context.get(), m -> m.put(key, value));
    }
}
