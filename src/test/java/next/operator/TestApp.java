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

package next.operator;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootApplication(
    exclude = {LineBotAutoConfiguration.class}
)
public class TestApp {

  public static void main(String[] args) {
    SpringApplication.run(TestApp.class, args);
  }

  // 為了避免test真的去call line api導致出錯，這邊要塞一個假的client進去
  @Bean
  @Primary
  public LineMessagingClient lineMessagingClient() {
    return new LineMessagingClient() {
      private final Logger log = LoggerFactory.getLogger(this.getClass());

      @Override
      public CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage) {
        log.info("replyMessage:" + replyMessage);
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> pushMessage(PushMessage pushMessage) {
        log.info("pushMessage:" + pushMessage);
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> multicast(Multicast multicast) {
        log.info("multicast:" + multicast);
        return null;
      }

      @Override
      public CompletableFuture<MessageContentResponse> getMessageContent(String messageId) {
        log.info("getMessageContent:" + messageId);
        return null;
      }

      @Override
      public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentReplyMessages(String date) {
        return null;
      }

      @Override
      public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentPushMessages(String date) {
        return null;
      }

      @Override
      public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentMulticastMessages(String date) {
        return null;
      }

      @Override
      public CompletableFuture<UserProfileResponse> getProfile(String userId) {
        log.info("getProfile:" + userId);
        return null;
      }

      @Override
      public CompletableFuture<UserProfileResponse> getGroupMemberProfile(String groupId, String userId) {
        return null;
      }

      @Override
      public CompletableFuture<UserProfileResponse> getRoomMemberProfile(String roomId, String userId) {
        return null;
      }

      @Override
      public CompletableFuture<MembersIdsResponse> getGroupMembersIds(String groupId, String start) {
        return null;
      }

      @Override
      public CompletableFuture<MembersIdsResponse> getRoomMembersIds(String roomId, String start) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> leaveGroup(String groupId) {
        log.info("leaveGroup:" + groupId);
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> leaveRoom(String roomId) {
        log.info("leaveRoom:" + roomId);
        return null;
      }

      @Override
      public CompletableFuture<RichMenuResponse> getRichMenu(String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<RichMenuIdResponse> createRichMenu(RichMenu richMenu) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> deleteRichMenu(String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<RichMenuIdResponse> getRichMenuIdOfUser(String userId) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> linkRichMenuIdToUser(String userId, String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> linkRichMenuIdToUsers(List<String> userIds, String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUser(String userId) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> unlinkRichMenuIdFromUsers(List<String> userIds) {
        return null;
      }

      @Override
      public CompletableFuture<MessageContentResponse> getRichMenuImage(String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> setRichMenuImage(String richMenuId, String contentType, byte[] content) {
        return null;
      }

      @Override
      public CompletableFuture<RichMenuListResponse> getRichMenuList() {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> setDefaultRichMenu(String richMenuId) {
        return null;
      }

      @Override
      public CompletableFuture<RichMenuIdResponse> getDefaultRichMenuId() {
        return null;
      }

      @Override
      public CompletableFuture<BotApiResponse> cancelDefaultRichMenu() {
        return null;
      }

      @Override
      public CompletableFuture<IssueLinkTokenResponse> issueLinkToken(String userId) {
        return null;
      }
    };
  }

}
