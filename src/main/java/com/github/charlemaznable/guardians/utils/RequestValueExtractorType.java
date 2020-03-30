package com.github.charlemaznable.guardians.utils;

import static com.github.charlemaznable.core.lang.Condition.nullThen;
import static java.nio.charset.StandardCharsets.UTF_8;

public enum RequestValueExtractorType {

    PARAMETER {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestParameterExtractor(keyNames);
        }
    },
    PATH {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestPathVariableExtractor(keyNames);
        }
    },
    HEADER {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestHeaderExtractor(keyNames);
        }
    },
    COOKIE {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestCookieExtractor(keyNames);
        }
    },
    BODY {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyFormatExtractor(keyNames, parser, charsetName);
        }
    },
    BODY_RAW {
        @Override
        public final RequestValueExtractor extractor(
                Iterable<String> keyNames, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyRawExtractor(nullThen(charsetName, UTF_8::name));
        }
    };

    public final RequestValueExtractor extractor(Iterable<String> keyNames) {
        return extractor(keyNames, null, null);
    }

    public abstract RequestValueExtractor extractor(
            Iterable<String> keyNames, RequestBodyFormat parser, String charsetName);
}
