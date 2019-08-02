package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;

import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.google.common.base.Charsets.UTF_8;

public enum RequestValueExtractType {

    Parameter {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestParameterExtractor(keyName);
        }
    },
    Path {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestPathVariableExtractor(keyName);
        }
    },
    Header {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestHeaderExtractor(keyName);
        }
    },
    Cookie {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestCookieExtractor(keyName);
        }
    },
    Body {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestBodyFormatExtractor(keyName, parser, charsetName);
        }
    },
    BodyRaw {
        @Override
        public RequestValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestBodyRawExtractor(nullThen(charsetName, UTF_8::name));
        }
    };

    public RequestValueExtractor extractor(String keyName) {
        return extractor(keyName, null, null);
    }

    public abstract RequestValueExtractor extractor(
            String keyName, RequestBodyParser parser, String charsetName);
}
