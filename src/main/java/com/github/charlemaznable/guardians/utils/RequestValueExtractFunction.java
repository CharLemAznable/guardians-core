package com.github.charlemaznable.guardians.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

public interface RequestValueExtractFunction extends Function<HttpServletRequest, String> {

}
