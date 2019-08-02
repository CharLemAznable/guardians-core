package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.spring.MutableHttpServletRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.codec.Bytes.bytes;
import static com.github.charlemaznable.codec.Bytes.string;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Json;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Xml;
import static com.github.charlemaznable.guardians.utils.RequestKeyedValueExtractType.Body;
import static com.google.common.base.Charsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RequestBodyFormatExtractorTest {

    @Test
    public void testRequestBodyExtractorForm() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("key1=value1&key2=%E8%A1%A8%E5%8D%95&key3=&key4");

        val formatExtractor1 = new RequestBodyFormatExtractor("key1");
        assertEquals(Form, formatExtractor1.getParser());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.apply(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2");
        assertEquals(Form, formatExtractor2.getParser());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", formatExtractor2.apply(request));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3");
        assertEquals(Form, formatExtractor3.getParser());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.apply(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4");
        assertEquals(Form, formatExtractor4.getParser());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.apply(request));
    }

    @Test
    public void testRequestBodyExtractorJson() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest(), ISO_8859_1);
        request.setRequestBody(string(bytes("{\"key1\":\"value1\",\"key2\":\"表单\",\"key3\":\"\"}"), ISO_8859_1));

        val formatExtractor1 = new RequestBodyFormatExtractor("key1", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor1.getParser());
        assertEquals("ISO-8859-1", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.apply(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor2.getParser());
        assertEquals("ISO-8859-1", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", string(bytes(formatExtractor2.apply(request), ISO_8859_1)));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor3.getParser());
        assertEquals("ISO-8859-1", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.apply(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor4.getParser());
        assertEquals("ISO-8859-1", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.apply(request));
    }

    @Test
    public void testRequestBodyExtractorXml() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml><key1>value1</key1><key2>表单</key2><key3></key3></xml>");

        val formatExtractor1 = new RequestBodyFormatExtractor("key1", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor1.getParser());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.apply(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor2.getParser());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", formatExtractor2.apply(request));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor3.getParser());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.apply(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor4.getParser());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.apply(request));
    }
}
