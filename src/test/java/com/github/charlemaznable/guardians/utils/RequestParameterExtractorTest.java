package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Parameter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestParameterExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setParameter("key", "value");

        val extractor1 = (RequestParameterExtractor) Parameter.extractor("key");
        assertEquals("key", extractor1.getKeyName());
        assertEquals("value", extractor1.extract(request));

        val extractor2 = (RequestParameterExtractor) Parameter.extractor("none");
        assertEquals("none", extractor2.getKeyName());
        assertNull(extractor2.extract(request));
    }
}
