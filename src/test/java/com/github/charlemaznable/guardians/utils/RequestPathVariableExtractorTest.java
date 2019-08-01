package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.lang.Mapp;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestKeyedValueExtractType.Path;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.web.servlet.HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE;

public class RequestPathVariableExtractorTest {

    @Test
    public void testRequestParameterExtractor() {
        val request = new MockHttpServletRequest();
        request.setAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE, Mapp.of("key", "value"));

        val extractor1 = Path.extractor("key");
        assertEquals("key", extractor1.getKeyName());
        assertEquals("value", extractor1.extract(request));

        val extractor2 = Path.extractor("none");
        assertEquals("none", extractor2.getKeyName());
        assertNull(extractor2.extract(request));
    }
}
