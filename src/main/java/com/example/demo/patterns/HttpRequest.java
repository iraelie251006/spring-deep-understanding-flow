package com.example.demo.patterns;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class HttpRequest {
    private final String url;
    private final MethodEnum method;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final int retryCount;
    private final Map<String, String> headers;
    private final String body;

    public String getUrl() {
        return url;
    }

    public MethodEnum getMethod() {
        return method;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    private HttpRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.connectTimeoutMs = builder.connectTimeoutMs;
        this.readTimeoutMs = builder.readTimeoutMs;
        this.retryCount = builder.retryCount;
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.body = builder.body;
    }


    public static final class Builder {
        private final String url;
        private final MethodEnum method;
        private int connectTimeoutMs = 3000;
        private int readTimeoutMs = 5000;
        private int retryCount = 0;
        private Map<String, String> headers = new HashMap<>();
        private String body = null;

        public Builder(String url, MethodEnum method) {
            if (url == null || url.isBlank()) {
                throw new IllegalArgumentException("url is required");
            }
            this.url = url;
            this.method = Objects.requireNonNull(method, "method is required");
        }

        public Builder connectTimeoutMs(int connectTimeoutMs) {
            if (connectTimeoutMs <= 0) {
                throw new IllegalArgumentException("connectTimeoutMs must be > 0");
            }
            this.connectTimeoutMs = connectTimeoutMs;
            return this;
        }

        public Builder readTimeoutMs(int readTimeoutMs) {
            if (readTimeoutMs <= 0) {
                throw new IllegalArgumentException("readTimeoutMs must be > 0");
            }
            this.readTimeoutMs = readTimeoutMs;
            return this;
        }

        public Builder retryCount(int retryCount) {
            if (retryCount < 0 || retryCount > 5) {
                throw new IllegalArgumentException("retryCount must be 0–5, got: " + retryCount);
            }
            this.retryCount = retryCount;
            return this;
        }

        public Builder addHeader(String key, String value) {
            Objects.requireNonNull(key, "header key is required");
            Objects.requireNonNull(value, "header value is required");
            this.headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            if (method == MethodEnum.GET || method == MethodEnum.DELETE) {
                if (body != null && !body.isBlank()) {
                    throw new IllegalStateException(method + " request must not have body");
                }
            }
            if (method == MethodEnum.PUT || method == MethodEnum.POST) {
                if (body == null || body.isBlank()) {
                    throw new IllegalStateException(method + " request must have a non-empty body");
                }
            }
            return new HttpRequest(this);
        }

    }
}
