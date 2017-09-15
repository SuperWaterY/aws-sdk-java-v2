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

import static software.amazon.awssdk.utils.FunctionalUtils.invokeSafely;

import java.net.URI;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.annotation.Immutable;
import software.amazon.awssdk.annotation.SdkPublicApi;
import software.amazon.awssdk.utils.http.SdkHttpUtils;

@SdkPublicApi
@Immutable
public interface SdkHttpRequest extends SdkHttpHeaders {

    String protocol();

    String host();

    int port(); // TODO: never null?

    /**
     * Returns the path to the resource being requested.
     * <br />
     * If this is non-blank, the path will always start with '/' and end without '/' (eg. "/path").
     * If no path is specified, this will always return "".
     *
     * @return The path to the resource being requested.
     */
    String path();

    /**
     * Returns a map of all parameters in this request.
     * <br/>
     * Should never be null, if there are no parameters an empty map is returned.
     *
     * @return A map of all parameters in this request.
     */
    Map<String, List<String>> queryParameters();

    default URI toUri() {
        return invokeSafely(() -> new URI(protocol(), null, host(), port(), path(),
                                          SdkHttpUtils.encodeQueryParameters(queryParameters()), null));
    }

    /**
     * Returns the HTTP method (GET, POST, etc) to use when sending this
     * request.
     *
     * @return The HTTP method to use when sending this request.
     */
    SdkHttpMethod method();
}
