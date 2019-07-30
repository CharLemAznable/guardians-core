package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;

public interface KeyedStringValueExtractor {

    String extract(HttpServletRequest request);
}
