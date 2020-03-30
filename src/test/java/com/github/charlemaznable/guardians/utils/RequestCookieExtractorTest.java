package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.COOKIE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestCookieExtractorTest {

    @Test
    public void testRequestCookieExtractor() {
        val mockCookie = new MockCookie("key", "value");
        val request = new MockHttpServletRequest();
        request.setCookies(mockCookie);

        val extractor1 = (RequestCookieExtractor) COOKIE.extractor("key");
        assertEquals("key", extractor1.getKeyNames().get(0));
        assertEquals("value", extractor1.extract(request).get("key"));

        val extractor2 = (RequestCookieExtractor) COOKIE.extractor("none");
        assertEquals("none", extractor2.getKeyNames().get(0));
        assertNull(extractor2.extract(request).get("none"));
    }
}
