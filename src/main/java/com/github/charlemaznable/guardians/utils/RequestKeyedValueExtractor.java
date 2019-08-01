package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;

public interface RequestKeyedValueExtractor {

    String getKeyName();

    String extract(HttpServletRequest request);
}
