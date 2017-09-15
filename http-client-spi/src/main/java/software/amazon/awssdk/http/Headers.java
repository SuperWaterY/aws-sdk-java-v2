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

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utilities and constants for working with HTTP headers.
 */
public final class Headers {

    public static final String CONTENT_LENGTH = "Content-Length";

    private Headers() {
    }

    /**
     * Perform a case-insensitive search for a particular header in the provided map of headers.
     *
     * @param headers The headers to search.
     * @param header The header to search for (case insensitively).
     * @return A stream providing the values for the headers that matched the requested header.
     */
    public static Stream<String> allMatching(Map<String, List<String>> headers, String header) {
        return headers.entrySet().stream()
                      .filter(e -> e.getKey().equalsIgnoreCase(header))
                      .flatMap(e -> e.getValue() != null ? e.getValue().stream() : Stream.empty());
    }

    /**
     * Perform a case-insensitive search for a particular header in the provided map of headers, returning the first matching
     * header, if one is found.
     * <br/>
     * This is useful for headers like 'Content-Type' or 'Content-Length' of which there is expected to be only one value present.
     *
     * @param headers The headers to search.
     * @param header The header to search for (case insensitively).
     * @return The first header that matched the requested one, or empty if one was not found.
     */
    public static Optional<String> firstMatching(Map<String, List<String>> headers, String header) {
        return allMatching(headers, header).findFirst();
    }
}
