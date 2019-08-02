package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.guardians.utils.RequestKeyedValueExtractType.Header;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestHeaderExtractorTest {

    @Test
    public void testRequestHeaderExtractor() {
        val request = new MockHttpServletRequest();
        request.addHeader("key", "value");

        val extractor1 = Header.extractor("key");
        assertEquals("key", extractor1.getKeyName());
        assertEquals("value", extractor1.apply(request));

        val extractor2 = Header.extractor("none");
        assertEquals("none", extractor2.getKeyName());
        assertNull(extractor2.apply(request));
    }
}
