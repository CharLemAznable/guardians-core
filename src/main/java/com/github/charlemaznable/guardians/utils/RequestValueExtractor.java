package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface RequestValueExtractor {

    Map<String, Object> extract(HttpServletRequest request);

    Object extractValue(HttpServletRequest request);
}
