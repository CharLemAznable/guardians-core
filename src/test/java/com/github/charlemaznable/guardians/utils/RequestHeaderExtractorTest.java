package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestHeaderExtractorTest {

    @Test
    public void testRequestHeaderExtractor() {
        val request = new MockHttpServletRequest();
        request.addHeader("key", "value");

        val extractor = new RequestHeaderExtractor("key");
        assertEquals("key", extractor.getHeaderName());
        assertEquals("value", extractor.apply(request));
        extractor.setHeaderName("none");
        assertEquals("none", extractor.getHeaderName());
        assertNull(extractor.apply(request));
    }
}
