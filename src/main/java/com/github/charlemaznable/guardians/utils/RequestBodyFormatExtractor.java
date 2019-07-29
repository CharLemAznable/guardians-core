package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.net.Http;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.github.charlemaznable.codec.Json.unJson;
import static com.github.charlemaznable.codec.Xml.unXml;
import static com.google.common.base.Charsets.UTF_8;

@Getter
public enum RequestBodyFormatExtractor {

    FORM {
        @Override
        public RequestBodyFormExtractor extractor() {
            return new RequestBodyFormExtractor();
        }
    },
    JSON {
        @Override
        public RequestBodyJsonExtractor extractor() {
            return new RequestBodyJsonExtractor();
        }
    },
    XML {
        @Override
        public RequestBodyXmlExtractor extractor() {
            return new RequestBodyXmlExtractor();
        }
    };

    public abstract AbstractRequestBodyFormatExtractor extractor();

    @Data
    static abstract class AbstractRequestBodyFormatExtractor implements Function<HttpServletRequest, Map> {

        private String charsetName = UTF_8.name();

        @Override
        public Map apply(HttpServletRequest request) {
            return parseRequestBody(Http.dealRequestBodyStream(request, charsetName));
        }

        protected abstract Map parseRequestBody(String requestBody);
    }

    static class RequestBodyFormExtractor extends AbstractRequestBodyFormatExtractor {

        @SneakyThrows
        @Override
        protected Map parseRequestBody(String requestBody) {
            Map<String, String> result = new HashMap<>();
            String[] pairs = StringUtils.tokenizeToStringArray(requestBody, "&");
            for (String pair : pairs) {
                int idx = pair.indexOf('=');
                if (idx == -1) {
                    result.put(URLDecoder.decode(pair, getCharsetName()), null);
                } else {
                    String name = URLDecoder.decode(pair.substring(0, idx), getCharsetName());
                    String value = URLDecoder.decode(pair.substring(idx + 1), getCharsetName());
                    result.put(name, value);
                }
            }
            return result;
        }
    }

    static class RequestBodyJsonExtractor extends AbstractRequestBodyFormatExtractor {

        @Override
        protected Map parseRequestBody(String requestBody) {
            return unJson(requestBody);
        }
    }

    static class RequestBodyXmlExtractor extends AbstractRequestBodyFormatExtractor {

        @Override
        protected Map parseRequestBody(String requestBody) {
            return unXml(requestBody);
        }
    }
}
