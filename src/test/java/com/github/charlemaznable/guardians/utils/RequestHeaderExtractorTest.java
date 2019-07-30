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
        assertEquals("key", extractor.getKeyName());
        assertEquals("value", extractor.extract(request));
        extractor.setKeyName("none");
        assertEquals("none", extractor.getKeyName());
        assertNull(extractor.extract(request));
    }
}
