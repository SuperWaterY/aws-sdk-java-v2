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

import static java.util.Collections.singletonList;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.annotation.Immutable;
import software.amazon.awssdk.annotation.ReviewBeforeRelease;
import software.amazon.awssdk.annotation.SdkPublicApi;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * Represents a request being sent to a service, including the parameters being sent as part of the request, the
 * endpoint to which the request should be sent, etc.
 *
 * <p>All implementations of this interface MUST be immutable. Instead of implementing this interface, consider using
 * {@link #builder()} to create one.</p>
 */
@SdkPublicApi
@Immutable
public interface SdkHttpFullRequest
        extends SdkHttpRequest, ToCopyableBuilder<SdkHttpFullRequest.Builder, SdkHttpFullRequest> {
    /**
     * @return Builder instance to construct a {@link DefaultSdkHttpFullRequest}.
     */
    static Builder builder() {
        return new DefaultSdkHttpFullRequest.Builder();
    }

    /**
     * Returns the optional stream containing the payload data to include for this request.
     * <br/>
     * Not all requests will contain payload data.
     *
     * @return The optional stream containing the payload data to include for this request or null if there is no payload.
     */
    @ReviewBeforeRelease("Optional instead of null?")
    InputStream content();

    /**
     * Builder interface for {@link SdkHttpFullRequest}.
     */
    @ReviewBeforeRelease("Extending SdkHttpRequest is dangerous, because this is mutable, but it's expected that "
                         + "requests aren't.")
    interface Builder extends CopyableBuilder<Builder, SdkHttpFullRequest> {
        String protocol();

        Builder protocol(String protocol);

        String host();

        Builder host(String host);

        Integer port();

        Builder port(Integer port);

        /**
         * Returns the path to the resource being requested.
         *
         * @return The path to the resource being requested.
         */
        String resourcePath();

        /**
         * Sets the resource path on the builder.
         *
         * @param resourcePath New resource path.
         * @return This builder for method chaining.
         */
        Builder resourcePath(String resourcePath);

        /**
         * Returns a map of all parameters in this request.
         * <br/>
         * Should never be null, if there are no parameters an empty map is returned.
         *
         * @return A map of all parameters in this request.
         */
        @ReviewBeforeRelease("Should this be 'query parameters'? It took me a while to figure out what an HTTP parameter was.")
        Map<String, List<String>> queryParameters();

        /**
         * Adds the query parameter to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing for that parameter, it does a complete
         * overwrite of the parameter. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param paramName  Name of the parameter to add
         * @param paramValue Value for the query param.
         * @return This builder for method chaining.
         */
        default Builder queryParameter(String paramName, String paramValue) {
            return queryParameter(paramName, singletonList(paramValue));
        }

        /**
         * Adds the query parameter with multiple values to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing for that parameter, it does a complete
         * overwrite of the parameter. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param paramName   Name of the parameter to add
         * @param paramValues List of values associated with this query parameter.
         * @return This builder for method chaining.
         */
        Builder queryParameter(String paramName, List<String> paramValues);

        /**
         * Adds the query parameters to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing,  it does a complete
         * overwrite of each query parameter in the map. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param queryParameters Query parameters to add
         * @return This builder for method chaining.
         */
        Builder queryParameters(Map<String, List<String>> queryParameters);

        /**
         * Removes all values for the query paramter from the builder.
         *
         * @param paramName Query param to remove.
         * @return This builder for method chaining.
         */
        Builder removeQueryParameter(String paramName);

        /**
         * Removes all query parameters from the builder.
         *
         * @return This builder for method chaining.
         */
        Builder clearQueryParameters();

        /**
         * Returns the service endpoint (ex: "https://ec2.amazonaws.com") to which
         * this request should be sent.
         *
         * @return The service endpoint to which this request should be sent.
         */
        @Deprecated
        URI endpoint();

        /**
         * Sets the endpoint for the builder.
         *
         * @param endpoint New endpoint.
         * @return This builder for method chaining.
         */
        @Deprecated
        default Builder endpoint(URI endpoint) {
            return protocol(endpoint.getScheme())
                    .host(endpoint.getHost())
                    .port(endpoint.getPort());
        }

        /**
         * Returns the HTTP method (GET, POST, etc) to use when sending this
         * request.
         *
         * @return The HTTP method to use when sending this request.
         */
        SdkHttpMethod httpMethod();

        /**
         * Sets the {@link SdkHttpMethod} for the builder.
         *
         * @param httpMethod New HTTP method.
         * @return This builder for method chaining.
         */
        Builder httpMethod(SdkHttpMethod httpMethod);

        /**
         * Returns the HTTP headers returned with this object.
         * <br/>
         * Should never be null, if there are no headers an empty map is returned
         *
         * @return The HTTP headers.
         */
        Map<String, List<String>> headers();

        /**
         * Adds the headers to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing for that header,  it does a complete
         * overwrite of each header key in the map. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param headers Headers to add
         * @return This builder for method chaining.
         */
        Builder headers(Map<String, List<String>> headers);

        /**
         * Adds the header to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing for that header,  it does a complete
         * overwrite of this header key. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param key   Header name
         * @param value Header value
         * @return This builder for method chaining.
         */
        default Builder header(String key, String value) {
            return header(key, singletonList(value));
        }

        /**
         * Adds the header values to the builder.
         *
         * <p>>Note that this does not merge with any values that may be pre-existing for that header,  it does a complete
         * overwrite of this header key. Merging must be handled by the caller if desired.
         * </p>
         *
         * @param key    Header name
         * @param values List of values associated with this header key.
         * @return This builder for method chaining.
         */
        Builder header(String key, List<String> values);

        /**
         * Sets the HTTP content for the builder.
         *
         * @param content New content.
         * @return This builder for method chaining.
         */
        Builder content(InputStream content);

        /**
         * Returns the optional stream containing the payload data to include for
         * this request.
         * <br/>
         * Not all requests will contain payload data.
         *
         * @return The optional stream containing the payload data to include for this request or null if there is no payload.
         */
        InputStream content();
    }

}
