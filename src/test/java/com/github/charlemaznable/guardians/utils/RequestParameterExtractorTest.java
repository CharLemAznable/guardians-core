package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.PARAMETER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestParameterExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setParameter("key", "value");

        val extractor1 = (RequestParameterExtractor) PARAMETER.extractor("key");
        assertEquals("key", extractor1.getKeyNames().get(0));
        assertEquals("value", extractor1.extractValue(request));

        val extractor2 = (RequestParameterExtractor) PARAMETER.extractor("none");
        assertEquals("none", extractor2.getKeyNames().get(0));
        assertNull(extractor2.extractValue(request));

        assertNull(PARAMETER.extractor(newArrayList("key", "none")).extractValue(request));
    }
}
