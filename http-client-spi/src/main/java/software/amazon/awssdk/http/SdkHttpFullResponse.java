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

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.annotation.ReviewBeforeRelease;
import software.amazon.awssdk.utils.builder.CopyableBuilder;
import software.amazon.awssdk.utils.builder.ToCopyableBuilder;

/**
 * Represents a response being returned from a service.
 *
 * <p>All implementations of this interface MUST be immutable. Instead of implementing this interface, consider using
 * {@link #builder()} to create one.</p>
 */
public interface SdkHttpFullResponse
        extends SdkHttpResponse, ToCopyableBuilder<SdkHttpFullResponse.Builder, SdkHttpFullResponse> {
    /**
     * @return Builder instance to construct a {@link DefaultSdkHttpFullResponse}.
     */
    static Builder builder() {
        return new DefaultSdkHttpFullResponse.Builder();
    }

    /**
     * Returns the input stream containing the response content. Instance of {@link AbortableInputStream}
     * which can be aborted before all content is read, this usually means killing the underlying HTTP
     * connection but it's up to HTTP implementations to define.
     * <br/>
     * May be null, not all responses have content.
     *
     * @return The input stream containing the response content or null if there is no content.
     */
    @ReviewBeforeRelease("Optional instead of null?")
    AbortableInputStream content();

    /**
     * Builder for a {@link DefaultSdkHttpFullResponse}.
     */
    interface Builder extends CopyableBuilder<Builder, SdkHttpFullResponse> {

        /**
         * Returns the HTTP status text associated with this response.
         *
         * @return The HTTP status text associated with this response.
         */
        String statusText();

        /**
         * TODO: comment and other helper methods for headers to match request?
         * @param statusText
         * @return
         */
        Builder statusText(String statusText);

        /**
         * Returns the HTTP status code (ex: 200, 404, etc) associated with this
         * response.
         *
         * @return The HTTP status code associated with this response.
         */
        int statusCode();

        Builder statusCode(int statusCode);

        /**
         * Returns the input stream containing the response content. Instance of {@link AbortableInputStream}
         * which can be aborted before all content is read, this usually means killing the underlying HTTP
         * connection but it's up to HTTP implementations to define.
         * <br/>
         * May be null, not all responses have content.
         *
         * @return The input stream containing the response content or null if there is no content.
         */
        AbortableInputStream content();

        Builder content(AbortableInputStream content);

        @ReviewBeforeRelease("Should we only allow setting the AbortableInputStream?")
        Builder content(InputStream content);

        /**
         * Returns the HTTP headers returned with this object.
         * <br/>
         * Should never be null, if there are no headers an empty map is returned
         *
         * @return The HTTP headers.
         */
        Map<String, List<String>> headers();

        Builder headers(Map<String, List<String>> headers);

        @ReviewBeforeRelease("This is completely different from the HTTP request. "
                             + "This should be the same between both interfaces.")
        Builder addHeader(String headerName, List<String> headerValues);
    }
}
