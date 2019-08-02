package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestKeyedValueExtractType.Cookie;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestCookieExtractorTest {

    @Test
    public void testRequestCookieExtractor() {
        val mockCookie = new MockCookie("key", "value");
        val request = new MockHttpServletRequest();
        request.setCookies(mockCookie);

        val extractor1 = Cookie.extractor("key");
        assertEquals("key", extractor1.getKeyName());
        assertEquals("value", extractor1.apply(request));

        val extractor2 = Cookie.extractor("none");
        assertEquals("none", extractor2.getKeyName());
        assertNull(extractor2.apply(request));
    }
}
