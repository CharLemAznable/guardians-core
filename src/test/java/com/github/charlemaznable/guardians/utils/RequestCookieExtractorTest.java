package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestCookieExtractorTest {

    @Test
    public void testRequestCookieExtractor() {
        val mockCookie = new MockCookie("key", "value");
        val request = new MockHttpServletRequest();
        request.setCookies(mockCookie);

        val extractor = new RequestCookieExtractor("key");
        assertEquals("key", extractor.getCookieName());
        assertEquals("value", extractor.extract(request));
        extractor.setCookieName("none");
        assertEquals("none", extractor.getCookieName());
        assertNull(extractor.extract(request));
    }
}
