package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;

public interface RequestValueExtractor {

    String extract(HttpServletRequest request);
}
