package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

public interface RequestValueExtractor extends Function<HttpServletRequest, String> {

}
