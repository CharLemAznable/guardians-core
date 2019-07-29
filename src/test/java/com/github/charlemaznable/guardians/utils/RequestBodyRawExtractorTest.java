package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.codec.Bytes.bytes;
import static com.google.common.base.Charsets.US_ASCII;
import static com.google.common.base.Charsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestBodyRawExtractorTest {

    @Test
    public void testRequestBodyRawExtractor() {
        val request = new MockHttpServletRequest();
        request.setContent(bytes("HTTP BODY CONTENT"));

        val rawExtractor = new RequestBodyRawExtractor(UTF_8.name());
        assertEquals("UTF-8", rawExtractor.getCharsetName());
        assertEquals("HTTP BODY CONTENT", rawExtractor.apply(request));

        request.setContent(bytes("HTTP BODY CONTENT", US_ASCII));

        rawExtractor.setCharsetName(US_ASCII.name());
        assertEquals("HTTP BODY CONTENT", rawExtractor.apply(request));
    }
}
