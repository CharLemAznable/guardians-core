package com.github.charlemaznable.guardians.utils;

import com.github.charlemaznable.guardians.utils.RequestBodyFormatExtractFunction.RequestBodyFormat;

import static com.github.charlemaznable.lang.Condition.nullThen;
import static com.google.common.base.Charsets.UTF_8;

public enum RequestValueExtractType {

    Parameter {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestParameterExtractFunction(keyName);
        }
    },
    Path {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestPathVariableExtractFunction(keyName);
        }
    },
    Header {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestHeaderExtractFunction(keyName);
        }
    },
    Cookie {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestCookieExtractFunction(keyName);
        }
    },
    Body {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyFormatExtractFunction(keyName, parser, charsetName);
        }
    },
    BodyRaw {
        @Override
        public RequestValueExtractFunction function(
                String keyName, RequestBodyFormat parser, String charsetName) {
            return new RequestBodyRawExtractFunction(nullThen(charsetName, UTF_8::name));
        }
    };

    public RequestValueExtractFunction function(String keyName) {
        return function(keyName, null, null);
    }

    public abstract RequestValueExtractFunction function(
            String keyName, RequestBodyFormat parser, String charsetName);
}
