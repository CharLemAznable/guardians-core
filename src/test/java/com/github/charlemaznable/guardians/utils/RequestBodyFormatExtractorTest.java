package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.exception.GuardianException;
import com.github.charlemaznable.spring.MutableHttpServletRequest;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.codec.Bytes.bytes;
import static com.github.charlemaznable.codec.Bytes.string;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Form;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Json;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.Xml;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.Body;
import static com.google.common.base.Charsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestBodyFormatExtractorTest {

    @Test
    public void testRequestBodyExtractorForm() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("key1=value1&key2=%E8%A1%A8%E5%8D%95&key3=&key4");

        val formatExtractor1 = new RequestBodyFormatExtractor("key1");
        assertEquals(Form, formatExtractor1.getFormat());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.extract(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2");
        assertEquals(Form, formatExtractor2.getFormat());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", formatExtractor2.extract(request));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3");
        assertEquals(Form, formatExtractor3.getFormat());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.extract(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4");
        assertEquals(Form, formatExtractor4.getFormat());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.extract(request));

        assertThrows(GuardianException.class, () -> Form.parse("key2=表单", ""));
    }

    @Test
    public void testRequestBodyExtractorJson() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest(), ISO_8859_1);
        request.setRequestBody(string(bytes("{\"key1\":\"value1\",\"key2\":\"表单\",\"key3\":\"\"}"), ISO_8859_1));

        val formatExtractor1 = new RequestBodyFormatExtractor("key1", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor1.getFormat());
        assertEquals("ISO-8859-1", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.extract(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor2.getFormat());
        assertEquals("ISO-8859-1", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", string(bytes(formatExtractor2.extract(request), ISO_8859_1)));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor3.getFormat());
        assertEquals("ISO-8859-1", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.extract(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4", Json, "ISO-8859-1");
        assertEquals(Json, formatExtractor4.getFormat());
        assertEquals("ISO-8859-1", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.extract(request));

        assertThrows(GuardianException.class, () -> Json.parse("\"key1\":\"value1\"", "UTF-8"));
    }

    @Test
    public void testRequestBodyExtractorXml() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml><key1>value1</key1><key2>表单</key2><key3></key3></xml>");

        val formatExtractor1 = new RequestBodyFormatExtractor("key1", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor1.getFormat());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyName());
        assertEquals("value1", formatExtractor1.extract(request));

        val formatExtractor2 = (RequestBodyFormatExtractor) Body.extractor("key2", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor2.getFormat());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyName());
        assertEquals("表单", formatExtractor2.extract(request));

        val formatExtractor3 = (RequestBodyFormatExtractor) Body.extractor("key3", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor3.getFormat());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyName());
        assertEquals("", formatExtractor3.extract(request));

        val formatExtractor4 = (RequestBodyFormatExtractor) Body.extractor("key4", Xml, "UTF-8");
        assertEquals(Xml, formatExtractor4.getFormat());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyName());
        assertNull(formatExtractor4.extract(request));

        assertThrows(GuardianException.class, () -> Xml.parse("\"key1\":\"value1\"", "UTF-8"));
    }
}
