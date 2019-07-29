package com.github.charlemaznable.guardians.utils;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static com.github.charlemaznable.codec.Bytes.bytes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyFormatExtractorTest {

    @Test
    public void testRequestBodyExtractorForm() {
        val request = new MockHttpServletRequest();
        request.setContent(bytes("key1=value1&key2=%E8%A1%A8%E5%8D%95&key3=&key4"));

        val formExtractor = RequestBodyFormatExtractor.Form.extractor();
        assertEquals("UTF-8", formExtractor.getCharsetName());
        Map formMap = formExtractor.apply(request);
        assertEquals("value1", formMap.get("key1"));
        assertEquals("表单", formMap.get("key2"));
        assertEquals("", formMap.get("key3"));
        assertNull(formMap.get("key4"));
    }

    @Test
    public void testRequestBodyExtractorJson() {
        val request = new MockHttpServletRequest();
        request.setContent(bytes("{\"key1\":\"value1\",\"key2\":\"表单\",\"key3\":\"\"}"));

        val jsonExtractor = RequestBodyFormatExtractor.Json.extractor();
        assertEquals("UTF-8", jsonExtractor.getCharsetName());
        Map formMap = jsonExtractor.apply(request);
        assertEquals("value1", formMap.get("key1"));
        assertEquals("表单", formMap.get("key2"));
        assertEquals("", formMap.get("key3"));
        assertNull(formMap.get("key4"));
    }

    @Test
    public void testRequestBodyExtractorXml() {
        val request = new MockHttpServletRequest();
        request.setContent(bytes("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml><key1>value1</key1><key2>表单</key2><key3></key3></xml>"));

        val xmlExtractor = RequestBodyFormatExtractor.Xml.extractor();
        assertEquals("UTF-8", xmlExtractor.getCharsetName());
        Map formMap = xmlExtractor.apply(request);
        assertEquals("value1", formMap.get("key1"));
        assertEquals("表单", formMap.get("key2"));
        assertEquals("", formMap.get("key3"));
        assertNull(formMap.get("key4"));
    }
}
