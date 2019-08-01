package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractor.RequestBodyParser;

public enum RequestKeyedValueExtractType {

    Parameter {
        @Override
        public RequestKeyedValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestParameterExtractor(keyName);
        }
    },
    Path {
        @Override
        public RequestKeyedValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestPathVariableExtractor(keyName);
        }
    },
    Header {
        @Override
        public RequestKeyedValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestHeaderExtractor(keyName);
        }
    },
    Cookie {
        @Override
        public RequestKeyedValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestCookieExtractor(keyName);
        }
    },
    Body {
        @Override
        public RequestKeyedValueExtractor extractor(
                String keyName, RequestBodyParser parser, String charsetName) {
            return new RequestBodyFormatExtractor(keyName, parser, charsetName);
        }
    };

    public RequestKeyedValueExtractor extractor(String keyName) {
        return extractor(keyName, null, null);
    }

    public abstract RequestKeyedValueExtractor extractor(
            String keyName, RequestBodyParser parser, String charsetName);
}
