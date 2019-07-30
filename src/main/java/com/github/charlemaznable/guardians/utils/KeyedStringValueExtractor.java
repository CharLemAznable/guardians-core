package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;

public interface KeyedStringValueExtractor {

    String getKeyName();

    void setKeyName(String keyName);

    String extract(HttpServletRequest request);
}
