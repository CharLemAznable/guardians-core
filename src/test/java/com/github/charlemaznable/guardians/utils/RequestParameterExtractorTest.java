package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestParameterExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setParameter("key", "value");

        val extractor = new RequestParameterExtractor("key");
        assertEquals("key", extractor.getKeyName());
        assertEquals("value", extractor.extract(request));
        extractor.setKeyName("none");
        assertEquals("none", extractor.getKeyName());
        assertNull(extractor.extract(request));
    }
}
