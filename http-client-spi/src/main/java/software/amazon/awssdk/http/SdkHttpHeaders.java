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

import java.util.List;
import java.util.Map;
import software.amazon.awssdk.annotation.Immutable;
import software.amazon.awssdk.annotation.SdkPublicApi;

@SdkPublicApi
@Immutable
public interface SdkHttpHeaders {
    /**
     * Returns the HTTP headers returned with this object.
     * <br/>
     * Should never be null, if there are no headers an empty map is returned
     *
     * @return The HTTP headers.
     */
    Map<String, List<String>> headers();
}
