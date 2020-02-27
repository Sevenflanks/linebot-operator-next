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

import java.util.List;

@Value
public class RichMenuResponse implements RichMenuCommonProperties {
    /**
     * Rich menu ID.
     */
    String richMenuId;

    /**
     * {@link RichMenuSize} object which contains the width and height of the rich menu displayed in the chat.
     *
     * <p>Rich menu images must be one of the following sizes: 2500x1686, 2500x843.
     */
    RichMenuSize size;

    /** true to display the rich menu by default. Otherwise, false. */
    boolean selected;

    /**
     * Name of the rich menu.
     * This value can be used to help manage your rich menus and is not displayed to users.
     *
     * <p>Maximum of 300 characters.
     */
    String name;

    /**
     * Text displayed in the chat bar.
     *
     * <p>Maximum of 14 characters.
     */
    String chatBarText;

    /**
     * Array of {@link RichMenuArea} objects which define the coordinates and size of tappable areas.
     *
     * <p>Maximum of 20 area objects.
     */
    List<RichMenuArea> areas;

    @JsonCreator
    public RichMenuResponse(@JsonProperty("richMenuId") final String richMenuId,
                            @JsonProperty("size") final RichMenuSize size,
                            @JsonProperty("selected") final boolean selected,
                            @JsonProperty("name") final String name,
                            @JsonProperty("chatBarText") final String chatBarText,
                            @JsonProperty("areas") final List<RichMenuArea> areas) {
        this.richMenuId = richMenuId;
        this.size = size;
        this.selected = selected;
        this.name = name;
        this.chatBarText = chatBarText;
        this.areas = areas;
    }
}
