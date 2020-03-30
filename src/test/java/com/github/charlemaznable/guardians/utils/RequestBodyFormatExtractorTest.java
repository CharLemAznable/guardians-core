package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.core.spring.MutableHttpServletRequest;
import com.github.charlemaznable.guardians.exception.GuardianException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static com.github.charlemaznable.core.codec.Bytes.bytes;
import static com.github.charlemaznable.core.codec.Bytes.string;
import static com.github.charlemaznable.core.lang.Listt.newArrayList;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.FORM;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.JSON;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormat.XML;
import static com.github.charlemaznable.guardians.utils.RequestValueExtractorType.BODY;
import static com.google.common.base.Charsets.ISO_8859_1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestBodyFormatExtractorTest {

    @Test
    public void testRequestBodyExtractorForm() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("key1=value1&key2=%E8%A1%A8%E5%8D%95&key3=&key4");

        val formatExtractor1 = new RequestBodyFormatExtractor(newArrayList("key1"));
        assertEquals(FORM, formatExtractor1.getFormat());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyNames().get(0));
        assertEquals("value1", formatExtractor1.extract(request).get("key1"));

        val formatExtractor2 = (RequestBodyFormatExtractor) BODY.extractor("key2");
        assertEquals(FORM, formatExtractor2.getFormat());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyNames().get(0));
        assertEquals("表单", formatExtractor2.extract(request).get("key2"));

        val formatExtractor3 = (RequestBodyFormatExtractor) BODY.extractor("key3");
        assertEquals(FORM, formatExtractor3.getFormat());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyNames().get(0));
        assertEquals("", formatExtractor3.extract(request).get("key3"));

        val formatExtractor4 = (RequestBodyFormatExtractor) BODY.extractor("key4");
        assertEquals(FORM, formatExtractor4.getFormat());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyNames().get(0));
        assertNull(formatExtractor4.extract(request).get("key4"));

        assertThrows(GuardianException.class, () -> FORM.parse("key2=表单", ""));
    }

    @Test
    public void testRequestBodyExtractorJson() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest(), ISO_8859_1);
        request.setRequestBody(string(bytes("{\"key1\":\"value1\",\"key2\":\"表单\",\"key3\":\"\"}"), ISO_8859_1));

        val formatExtractor1 = new RequestBodyFormatExtractor(newArrayList("key1"), JSON, "ISO-8859-1");
        assertEquals(JSON, formatExtractor1.getFormat());
        assertEquals("ISO-8859-1", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyNames().get(0));
        assertEquals("value1", formatExtractor1.extract(request).get("key1"));

        val formatExtractor2 = (RequestBodyFormatExtractor) BODY.extractor("key2", JSON, "ISO-8859-1");
        assertEquals(JSON, formatExtractor2.getFormat());
        assertEquals("ISO-8859-1", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyNames().get(0));
        assertEquals("表单", string(bytes(formatExtractor2.extract(request).get("key2").toString(), ISO_8859_1)));

        val formatExtractor3 = (RequestBodyFormatExtractor) BODY.extractor("key3", JSON, "ISO-8859-1");
        assertEquals(JSON, formatExtractor3.getFormat());
        assertEquals("ISO-8859-1", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyNames().get(0));
        assertEquals("", formatExtractor3.extract(request).get("key3"));

        val formatExtractor4 = (RequestBodyFormatExtractor) BODY.extractor("key4", JSON, "ISO-8859-1");
        assertEquals(JSON, formatExtractor4.getFormat());
        assertEquals("ISO-8859-1", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyNames().get(0));
        assertNull(formatExtractor4.extract(request).get("key4"));

        assertThrows(GuardianException.class, () -> JSON.parse("\"key1\":\"value1\"", "UTF-8"));
    }

    @Test
    public void testRequestBodyExtractorXml() {
        val request = new MutableHttpServletRequest(new MockHttpServletRequest());
        request.setRequestBody("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<xml><key1>value1</key1><key2>表单</key2><key3></key3></xml>");

        val formatExtractor1 = new RequestBodyFormatExtractor(newArrayList("key1"), XML, "UTF-8");
        assertEquals(XML, formatExtractor1.getFormat());
        assertEquals("UTF-8", formatExtractor1.getCharsetName());
        assertEquals("key1", formatExtractor1.getKeyNames().get(0));
        assertEquals("value1", formatExtractor1.extract(request).get("key1"));

        val formatExtractor2 = (RequestBodyFormatExtractor) BODY.extractor("key2", XML, "UTF-8");
        assertEquals(XML, formatExtractor2.getFormat());
        assertEquals("UTF-8", formatExtractor2.getCharsetName());
        assertEquals("key2", formatExtractor2.getKeyNames().get(0));
        assertEquals("表单", formatExtractor2.extract(request).get("key2"));

        val formatExtractor3 = (RequestBodyFormatExtractor) BODY.extractor("key3", XML, "UTF-8");
        assertEquals(XML, formatExtractor3.getFormat());
        assertEquals("UTF-8", formatExtractor3.getCharsetName());
        assertEquals("key3", formatExtractor3.getKeyNames().get(0));
        assertEquals("", formatExtractor3.extract(request).get("key3"));

        val formatExtractor4 = (RequestBodyFormatExtractor) BODY.extractor("key4", XML, "UTF-8");
        assertEquals(XML, formatExtractor4.getFormat());
        assertEquals("UTF-8", formatExtractor4.getCharsetName());
        assertEquals("key4", formatExtractor4.getKeyNames().get(0));
        assertNull(formatExtractor4.extract(request).get("key4"));

        assertThrows(GuardianException.class, () -> XML.parse("\"key1\":\"value1\"", "UTF-8"));
    }
}
