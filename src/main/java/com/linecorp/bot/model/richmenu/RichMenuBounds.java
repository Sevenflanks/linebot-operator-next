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

package com.linecorp.bot.model.richmenu;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class RichMenuBounds {
    /** Horizontal position relative to the top-left corner of the area. */
    int x;

    /** Vertical position relative to the top-left corner of the area. */
    int y;

    /** Width of the area. */
    int width;

    /** Height of the area. */
    int height;

    @JsonCreator
    public RichMenuBounds(@JsonProperty("x") final int x,
                          @JsonProperty("y") final int y,
                          @JsonProperty("width") final int width,
                          @JsonProperty("height") final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
