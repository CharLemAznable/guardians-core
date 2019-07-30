package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.lang.Mapp;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

public class RequestPathVariableExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, Mapp.of("key", "value"));

        val extractor = new RequestPathVariableExtractor("key");
        assertEquals("key", extractor.getKeyName());
        assertEquals("value", extractor.extract(request));
        extractor.setKeyName("none");
        assertEquals("none", extractor.getKeyName());
        assertNull(extractor.extract(request));
    }
}
