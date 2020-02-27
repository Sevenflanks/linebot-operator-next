/*
 * Copyright 2018 LINE Corporation
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

package com.linecorp.bot.model.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.linecorp.bot.model.event.link.LinkContent;
import com.linecorp.bot.model.event.source.Source;
import lombok.Value;

import java.time.Instant;

/**
 * Event object for when a user has linked his/her LINE account with a provider's service account.
 * You can reply to account link events.
 */
@Value
@JsonTypeName("accountLink")
public class AccountLinkEvent implements Event, ReplyEvent {
    /**
     * Token for replying to this event.
     */
    private final String replyToken;

    /**
     * JSON object which contains the source of the event.
     */
    private final Source source;

    /**
     * Time of the event.
     */
    private final Instant timestamp;

    /**
     * Content of the account link event.
     */
    private final LinkContent link;

    @JsonCreator
    public AccountLinkEvent(
            @JsonProperty("replyToken") String replyToken,
            @JsonProperty("source") Source source,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("link") LinkContent link) {
        this.replyToken = replyToken;
        this.source = source;
        this.timestamp = timestamp;
        this.link = link;
    }
}
