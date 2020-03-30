package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.core.lang.Mapp.map;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

public class RequestPathVariableExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, map("key", "value"));

        val extractor1 = (RequestPathVariableExtractor) PATH.extractor("key");
        assertEquals("key", extractor1.getKeyNames().get(0));
        assertEquals("value", extractor1.extractValue(request));

        val extractor2 = (RequestPathVariableExtractor) PATH.extractor("none");
        assertEquals("none", extractor2.getKeyNames().get(0));
        assertNull(extractor2.extractValue(request));

        assertNull(PATH.extractor(newArrayList("key", "none")).extractValue(request));
    }
}
