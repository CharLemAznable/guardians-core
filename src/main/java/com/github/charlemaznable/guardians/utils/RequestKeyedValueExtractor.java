package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;

public interface RequestKeyedValueExtractor {

    String getKeyName();

    void setKeyName(String keyName);

    String extract(HttpServletRequest request);
}
