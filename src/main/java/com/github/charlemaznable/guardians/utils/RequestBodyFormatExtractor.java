package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.net.Http;
import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.codec.Xml.unXml;
import static com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser.Form;
import static com.github.charlemaznable.lang.Mapp.getStr;
import static com.github.charlemaznable.lang.Mapp.newHashMap;
import static com.github.charlemaznable.lang.Str.isNotBlank;
import static com.google.common.base.Charsets.UTF_8;

@Getter
@RequiredArgsConstructor
public class RequestBodyFormatExtractor implements Function<HttpServletRequest, Map>, RequestKeyedValueExtractor {

    @NonNull
    private String keyName;
    private RequestBodyParser parser = Form;
    private String charsetName = UTF_8.name();

    public RequestBodyFormatExtractor(String keyName, RequestBodyParser parser, String charsetName) {
        this.keyName = keyName;
        if (null != parser) this.parser = parser;
        if (isNotBlank(charsetName)) this.charsetName = charsetName;
    }

    @Override
    public Map apply(HttpServletRequest request) {
        return parser.parse(Http.dealRequestBodyStream(request, charsetName), charsetName);
    }

    @Override
    public String extract(HttpServletRequest request) {
        return getStr(apply(request), keyName);
    }

    public enum RequestBodyParser {

        Form {
            @SneakyThrows
            @Override
            public Map parse(String requestBody, String charsetName) {
                val result = new HashMap<String, String>();
                Iterable<String> pairs = Splitter.on("&").split(requestBody);
                for (val pair : pairs) {
                    int idx = pair.indexOf('=');
                    if (idx == -1) {
                        result.put(URLDecoder.decode(pair, charsetName), null);
                    } else {
                        String name = URLDecoder.decode(pair.substring(0, idx), charsetName);
                        String value = URLDecoder.decode(pair.substring(idx + 1), charsetName);
                        result.put(name, value);
                    }
                }
                return result;
            }
        },
        Json {
            @Override
            public Map parse(String requestBody, String charsetName) {
                return newHashMap(unJson(requestBody));
            }
        },
        Xml {
            @Override
            public Map parse(String requestBody, String charsetName) {
                return newHashMap(unXml(requestBody));
            }
        };

        public abstract Map parse(String requestBody, String charsetName);
    }
}
