/*
 * Copyright 2010-2017 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.http;

import static software.amazon.awssdk.utils.CollectionUtils.deepUnmodifiableMap;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import software.amazon.awssdk.annotation.Immutable;
import software.amazon.awssdk.annotation.ReviewBeforeRelease;
import software.amazon.awssdk.annotation.SdkInternalApi;
import software.amazon.awssdk.utils.CollectionUtils;
import software.amazon.awssdk.utils.Validate;
import software.amazon.awssdk.utils.http.SdkHttpUtils;

/**
 * Internal implementation of {@link SdkHttpFullRequest}, buildable via {@link SdkHttpFullRequest#builder()}. Provided to HTTP
 * implementation to execute a request.
 */
@SdkInternalApi
@Immutable
class DefaultSdkHttpFullRequest implements SdkHttpFullRequest {
    private final String protocol;
    private final String host;
    private final Integer port;
    private final String resourcePath;
    private final Map<String, List<String>> queryParameters;
    private final SdkHttpMethod httpMethod;
    private final Map<String, List<String>> headers;
    private final InputStream content;

    private DefaultSdkHttpFullRequest(Builder builder) {
        this.protocol = Validate.paramNotNull(builder.protocol, "protocol");
        this.host = Validate.paramNotNull(builder.host, "host");
        this.port = builder.port;
        this.resourcePath = standardizePath(builder.resourcePath);
        this.queryParameters = deepUnmodifiableMap(builder.queryParameters, () -> new LinkedHashMap<>());
        this.httpMethod = Validate.paramNotNull(builder.httpMethod, "method");
        this.headers = deepUnmodifiableMap(builder.headers, () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER));
        this.content = builder.content;
    }

    private String standardizePath(String path) {
        if (path == null) {
            return "";
        }

        StringBuilder standardizedPath = new StringBuilder();

        // Path must always start with '/' (unless blank)
        if (!path.startsWith("/")) {
            standardizedPath.append('/');
        }

        standardizedPath.append(path);

        // Path must never end with '/'
        if (path.endsWith("/")) {
            standardizedPath.setLength(standardizedPath.length() - 1);
        }

        return standardizedPath.toString();
    }

    @Override
    public String protocol() {
        return protocol;
    }

    @Override
    public String host() {
        return host;
    }

    @Override
    public Integer port() {
        return Optional.ofNullable(port).orElseGet(() -> SdkHttpUtils.standardPort(protocol()));
    }

    @Override
    public Map<String, List<String>> headers() {
        return headers;
    }

    @Override
    public String path() {
        return resourcePath;
    }

    @Override
    public Map<String, List<String>> queryParameters() {
        return queryParameters;
    }

    @Override
    public SdkHttpMethod method() {
        return httpMethod;
    }

    @Override
    public Optional<InputStream> content() {
        return Optional.ofNullable(content);
    }

    @Override
    public SdkHttpFullRequest.Builder toBuilder() {
        return new Builder()
                .protocol(protocol)
                .host(host)
                .port(port)
                .path(resourcePath)
                .queryParameters(queryParameters)
                .method(httpMethod)
                .headers(headers)
                .content(content);
    }

    /**
     * Builder for a {@link DefaultSdkHttpFullRequest}.
     */
    static final class Builder implements SdkHttpFullRequest.Builder {

        private String protocol;
        private String host;
        private Integer port;
        private String resourcePath;
        @ReviewBeforeRelease("Do we need linked hash map here?")
        private Map<String, List<String>> queryParameters = new LinkedHashMap<>();
        private SdkHttpMethod httpMethod;
        private Map<String, List<String>> headers = new HashMap<>();
        private InputStream content;

        Builder() {
        }

        @Override
        public String protocol() {
            return protocol;
        }

        @Override
        public SdkHttpFullRequest.Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        @Override
        public String host() {
            return host;
        }

        @Override
        public SdkHttpFullRequest.Builder host(String host) {
            this.host = host;
            return this;
        }

        @Override
        public Integer port() {
            return port;
        }

        @Override
        public SdkHttpFullRequest.Builder port(Integer port) {
            this.port = port;
            return this;
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder path(String path) {
            this.resourcePath = path;
            return this;
        }

        @Override
        public String path() {
            return resourcePath;
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder queryParameter(String paramName, List<String> paramValues) {
            this.queryParameters.put(paramName, new ArrayList<>(paramValues));
            return this;
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder queryParameters(Map<String, List<String>> queryParameters) {
            this.queryParameters = CollectionUtils.deepCopyMap(queryParameters, () -> new LinkedHashMap<>());
            return this;
        }

        @Override
        public Builder removeQueryParameter(String paramName) {
            this.queryParameters.remove(paramName);
            return this;
        }

        @Override
        public Builder clearQueryParameters() {
            this.queryParameters.clear();
            return this;
        }

        @Override
        public Map<String, List<String>> queryParameters() {
            return CollectionUtils.deepUnmodifiableMap(this.queryParameters);
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder method(SdkHttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        @Override
        public SdkHttpMethod method() {
            return httpMethod;
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder header(String key, List<String> values) {
            this.headers.put(key, new ArrayList<>(values));
            return this;
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder headers(Map<String, List<String>> headers) {
            this.headers = CollectionUtils.deepCopyMap(headers);
            return this;
        }

        @Override
        public Map<String, List<String>> headers() {
            return CollectionUtils.deepUnmodifiableMap(this.headers);
        }

        @Override
        public DefaultSdkHttpFullRequest.Builder content(InputStream content) {
            this.content = content;
            return this;
        }

        @Override
        public InputStream content() {
            return content;
        }

        /**
         * @return An immutable {@link DefaultSdkHttpFullRequest} object.
         */
        @Override
        public DefaultSdkHttpFullRequest build() {
            return new DefaultSdkHttpFullRequest(this);
        }
    }

}
