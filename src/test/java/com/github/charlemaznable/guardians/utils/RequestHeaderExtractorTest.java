package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestHeaderExtractorTest {

    @Test
    public void testRequestHeaderExtractor() {
        val request = new MockHttpServletRequest();
        request.addHeader("key", "value");

        val extractor1 = (RequestHeaderExtractor) HEADER.extractor("key");
        assertEquals("key", extractor1.getKeyName());
        assertEquals("value", extractor1.extract(request));

        val extractor2 = (RequestHeaderExtractor) HEADER.extractor("none");
        assertEquals("none", extractor2.getKeyName());
        assertNull(extractor2.extract(request));
    }
}
