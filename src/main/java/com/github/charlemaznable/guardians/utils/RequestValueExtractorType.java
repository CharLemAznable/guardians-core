package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyFormat;

import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static java.nio.charset.StandardCharsets.UTF_8;

public enum RequestValueExtractorType {

    PARAMETER {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestParameterExtractor(keyName);
        }
    },
    PATH {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestPathVariableExtractor(keyName);
        }
    },
    HEADER {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestHeaderExtractor(keyName);
        }
    },
    COOKIE {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestCookieExtractor(keyName);
        }
    },
    BODY {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyFormatExtractor(keyName, parser, charsetName);
        }
    },
    BODY_RAW {
        @Override
        public final RequestValueExtractor extractor(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyRawExtractor(nullThen(charsetName, UTF_8::name));
        }
    };

    public final RequestValueExtractor extractor(String keyName) {
        return extractor(keyName, null, null);
    }

    public abstract RequestValueExtractor extractor(
            String keyName, RequestBodyFormat parser, String charsetName);
}
