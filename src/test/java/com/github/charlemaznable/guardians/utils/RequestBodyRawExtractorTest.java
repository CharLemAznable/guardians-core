package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.core.codec.Bytes.bytes;
import static com.github.charlemaznable.core.codec.Bytes.string;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BodyRaw;
import static com.google.common.base.Charsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestBodyRawExtractorTest {

    @Test
    public void testRequestBodyRawExtractor() {
        val request = new MockHttpServletRequest();
        request.setContent(bytes("HTTP BODY 内容"));

        val rawExtractor1 = (RequestBodyRawExtractor) BodyRaw.extractor("");
        assertEquals("UTF-8", rawExtractor1.getCharsetName());
        assertEquals("HTTP BODY 内容", rawExtractor1.extract(request));

        request.setContent(bytes("HTTP BODY 内容"));

        val rawExtractor2 = (RequestBodyRawExtractor) BodyRaw.extractor("", null, ISO_8859_1.name());
        assertEquals("ISO-8859-1", rawExtractor2.getCharsetName());
        assertEquals("HTTP BODY 内容", string(bytes(rawExtractor2.extract(request), ISO_8859_1)));
    }
}
