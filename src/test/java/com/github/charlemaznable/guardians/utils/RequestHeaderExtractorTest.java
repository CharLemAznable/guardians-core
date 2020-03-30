package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestHeaderExtractorTest {

    @Test
    public void testRequestHeaderExtractor() {
        val request = new MockHttpServletRequest();
        request.addHeader("key", "value");

        val extractor1 = (RequestHeaderExtractor) HEADER.extractor(newArrayList("key"));
        assertEquals("key", extractor1.getKeyNames().get(0));
        assertEquals("value", extractor1.extract(request).get("key"));

        val extractor2 = (RequestHeaderExtractor) HEADER.extractor(newArrayList("none"));
        assertEquals("none", extractor2.getKeyNames().get(0));
        assertNull(extractor2.extract(request).get("none"));
    }
}
