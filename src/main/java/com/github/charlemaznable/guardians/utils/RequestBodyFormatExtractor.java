package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.exception.GuardianException;
import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.github.charlemaznable.core.codec.Json.unJson;
import static com.github.charlemaznable.core.codec.Xml.unXml;
import static com.github.charlemaznable.core.lang.Mapp.getStr;
import static com.github.charlemaznable.core.lang.Mapp.newHashMap;
import static com.github.charlemaznable.core.lang.Str.isNotBlank;
import static com.github.charlemaznable.core.net.Http.dealRequestBodyStream;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat.FORM;
import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
public final class RequestBodyFormatExtractor implements RequestValueExtractor {

    private String keyName;
    private RequestBodyFormat format = FORM;
    private String charsetName = UTF_8.name();

    public RequestBodyFormatExtractor(String keyName) {
        this.keyName = keyName;
    }

    public RequestBodyFormatExtractor(String keyName, RequestBodyFormat format, String charsetName) {
        this.keyName = keyName;
        if (null != format) this.format = format;
        if (isNotBlank(charsetName)) this.charsetName = charsetName;
    }

    @Override
    public String extract(HttpServletRequest request) {
        val requestBody = dealRequestBodyStream(request, charsetName);
        return getStr(format.parse(requestBody, charsetName), keyName);
    }

    public enum RequestBodyFormat {

        FORM {
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                try {
                    val result = new HashMap<String, Object>();
                    // Error:java:
                    // Lombok visitor handler class lombok.javac.handlers.HandleVal failed:
                    // java.lang.NullPointerException
                    Iterable<String> pairs = Splitter.on("&").split(requestBody);
                    for (val pair : pairs) {
                        int idx = pair.indexOf('=');
                        if (idx == -1) {
                            result.put(decode(pair, charsetName), null);
                        } else {
                            // Error:java:
                            // Lombok visitor handler class lombok.javac.handlers.HandleVal failed:
                            // java.lang.NullPointerException
                            String name = decode(pair.substring(0, idx), charsetName);
                            String value = decode(pair.substring(idx + 1), charsetName);
                            result.put(name, value);
                        }
                    }
                    return result;
                } catch (Exception e) {
                    throw new GuardianException(e);
                }
            }
        },
        JSON {
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                try {
                    return newHashMap(unJson(requestBody));
                } catch (Exception e) {
                    throw new GuardianException(e);
                }
            }
        },
        XML {
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                try {
                    return newHashMap(unXml(requestBody));
                } catch (Exception e) {
                    throw new GuardianException(e);
                }
            }
        };

        public abstract Map<String, Object> parse(String requestBody, String charsetName);
    }
}
