package com.github.charlemaznable.guardians.utils;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.codec.Xml.unXml;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractFunction.RequestBodyFormat.Form;
import static com.github.charlemaznable.lang.Mapp.getStr;
import static com.github.charlemaznable.lang.Mapp.newHashMap;
import static com.github.charlemaznable.lang.Str.isNotBlank;
import static com.github.charlemaznable.net.Http.dealRequestBodyStream;
import static com.google.common.base.Charsets.UTF_8;
import static java.net.URLDecoder.decode;

@Getter
@RequiredArgsConstructor
public class RequestBodyFormatExtractFunction implements RequestValueExtractFunction {

    @NonNull
    private String keyName;
    private RequestBodyFormat format = Form;
    private String charsetName = UTF_8.name();

    public RequestBodyFormatExtractFunction(String keyName, RequestBodyFormat format, String charsetName) {
        this.keyName = keyName;
        if (null != format) this.format = format;
        if (isNotBlank(charsetName)) this.charsetName = charsetName;
    }

    @Override
    public String apply(HttpServletRequest request) {
        val requestBody = dealRequestBodyStream(request, charsetName);
        return getStr(format.parse(requestBody, charsetName), keyName);
    }

    public enum RequestBodyFormat {

        Form {
            @SneakyThrows
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                val result = new HashMap<String, Object>();
                Iterable<String> pairs = Splitter.on("&").split(requestBody);
                for (val pair : pairs) {
                    int idx = pair.indexOf('=');
                    if (idx == -1) {
                        result.put(decode(pair, charsetName), null);
                    } else {
                        String name = decode(pair.substring(0, idx), charsetName);
                        String value = decode(pair.substring(idx + 1), charsetName);
                        result.put(name, value);
                    }
                }
                return result;
            }
        },
        Json {
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                return newHashMap(unJson(requestBody));
            }
        },
        Xml {
            @Override
            public Map<String, Object> parse(String requestBody, String charsetName) {
                return newHashMap(unXml(requestBody));
            }
        };

        public abstract Map<String, Object> parse(String requestBody, String charsetName);
    }
}