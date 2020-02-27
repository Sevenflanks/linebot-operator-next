/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.model.error;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.Collections;
import java.util.List;

/**
 * Error response from LINE Messaging Server.
 *
 * @see <a href="https://developers.line.me/en/reference/messaging-api/#error-responses">//developers.line.me/en/reference/messaging-api/#error-responses</a>
 */
@Value
public class ErrorResponse {
    /** Request ID in response header. */
    String requestId;

    /** Summary or details of the error. */
    String message;

    /**
     * Details of the error.
     *
     * <p>Always non-null but can be empty.
     */
    List<ErrorDetail> details;

    public ErrorResponse(
            @JacksonInject("requestId") final String requestId,
            @JsonProperty("message") final String message,
            @JsonProperty("details") final List<ErrorDetail> details) {
        this.requestId = requestId;
        this.message = message;
        this.details = details != null ? details : Collections.emptyList();
    }
}
