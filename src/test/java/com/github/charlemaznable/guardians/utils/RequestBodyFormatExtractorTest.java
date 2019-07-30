package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyFormatExtractorTest {

    @Test
    public void testRequestBodyExtractorForm() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("key1=value1&key2=%E8%A1%A8%E5%8D%95&key3=&key4");

        val formatExtractor = new RequestBodyFormatExtractor("key1");
        assertEquals("UTF-8", formatExtractor.getCharsetName());
        assertEquals("key1", formatExtractor.getKeyName());
        assertEquals("value1", formatExtractor.extract(request));

        formatExtractor.setKeyName("key2");
        assertEquals("key2", formatExtractor.getKeyName());
        assertEquals("表单", formatExtractor.extract(request));

        formatExtractor.setKeyName("key3");
        assertEquals("key3", formatExtractor.getKeyName());
        assertEquals("", formatExtractor.extract(request));

        formatExtractor.setKeyName("key4");
        assertEquals("key4", formatExtractor.getKeyName());
        assertNull(formatExtractor.extract(request));
    }

    @Test
    public void testRequestBodyExtractorJson() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("{\"key1\":\"value1\",\"key2\":\"表单\",\"key3\":\"\"}");

        val formatExtractor = new RequestBodyFormatExtractor("key1");
        formatExtractor.setParser(RequestBodyParser.Json);
        assertEquals(RequestBodyParser.Json, formatExtractor.getParser());
        assertEquals("UTF-8", formatExtractor.getCharsetName());
        assertEquals("key1", formatExtractor.getKeyName());
        assertEquals("value1", formatExtractor.extract(request));

        formatExtractor.setKeyName("key2");
        assertEquals("key2", formatExtractor.getKeyName());
        assertEquals("表单", formatExtractor.extract(request));

        formatExtractor.setKeyName("key3");
        assertEquals("key3", formatExtractor.getKeyName());
        assertEquals("", formatExtractor.extract(request));

        formatExtractor.setKeyName("key4");
        assertEquals("key4", formatExtractor.getKeyName());
        assertNull(formatExtractor.extract(request));
    }

    @Test
    public void testRequestBodyExtractorXml() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml><key1>value1</key1><key2>表单</key2><key3></key3></xml>");

        val formatExtractor = new RequestBodyFormatExtractor("key1");
        formatExtractor.setParser(RequestBodyParser.Xml);
        assertEquals(RequestBodyParser.Xml, formatExtractor.getParser());
        assertEquals("UTF-8", formatExtractor.getCharsetName());
        assertEquals("key1", formatExtractor.getKeyName());
        assertEquals("value1", formatExtractor.extract(request));

        formatExtractor.setKeyName("key2");
        assertEquals("key2", formatExtractor.getKeyName());
        assertEquals("表单", formatExtractor.extract(request));

        formatExtractor.setKeyName("key3");
        assertEquals("key3", formatExtractor.getKeyName());
        assertEquals("", formatExtractor.extract(request));

        formatExtractor.setKeyName("key4");
        assertEquals("key4", formatExtractor.getKeyName());
        assertNull(formatExtractor.extract(request));
    }
}
