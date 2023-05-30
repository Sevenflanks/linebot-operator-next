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
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.Multicast;
import com.linecorp.bot.model.Narrowcast;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
import com.linecorp.bot.model.profile.MembersIdsResponse;
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.request.GetFollowersRequest;
import com.linecorp.bot.model.request.SetWebhookEndpointRequest;
import com.linecorp.bot.model.request.TestWebhookEndpointRequest;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.response.BotInfoResponse;
import com.linecorp.bot.model.response.GetAggregationUnitNameListResponse;
import com.linecorp.bot.model.response.GetAggregationUnitUsageResponse;
import com.linecorp.bot.model.response.GetFollowersResponse;
import com.linecorp.bot.model.response.GetMessageEventResponse;
import com.linecorp.bot.model.response.GetNumberOfFollowersResponse;
import com.linecorp.bot.model.response.GetNumberOfMessageDeliveriesResponse;
import com.linecorp.bot.model.response.GetStatisticsPerUnitResponse;
import com.linecorp.bot.model.response.GetWebhookEndpointResponse;
import com.linecorp.bot.model.response.IssueLinkTokenResponse;
import com.linecorp.bot.model.response.MessageQuotaResponse;
import com.linecorp.bot.model.response.NarrowcastProgressResponse;
import com.linecorp.bot.model.response.NumberOfMessagesResponse;
import com.linecorp.bot.model.response.QuotaConsumptionResponse;
import com.linecorp.bot.model.response.SetWebhookEndpointResponse;
import com.linecorp.bot.model.response.TestWebhookEndpointResponse;
import com.linecorp.bot.model.response.demographics.GetFriendsDemographicsResponse;
import com.linecorp.bot.model.richmenu.RichMenu;
import com.linecorp.bot.model.richmenu.RichMenuIdResponse;
import com.linecorp.bot.model.richmenu.RichMenuListResponse;
import com.linecorp.bot.model.richmenu.RichMenuResponse;
import com.linecorp.bot.model.richmenualias.CreateRichMenuAliasRequest;
import com.linecorp.bot.model.richmenualias.RichMenuAliasListResponse;
import com.linecorp.bot.model.richmenualias.RichMenuAliasResponse;
import com.linecorp.bot.model.richmenualias.UpdateRichMenuAliasRequest;
import com.linecorp.bot.model.room.RoomMemberCountResponse;
import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;
import com.linecorp.bot.spring.boot.LineBotProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootApplication(
    exclude = {LineBotAutoConfiguration.class}
)
@EnableConfigurationProperties(LineBotProperties.class)
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

      @Override public CompletableFuture<BotApiResponse> broadcast(Broadcast broadcast) {
        return null;
      }

      @Override public CompletableFuture<BotApiResponse> narrowcast(Narrowcast broadcast) {
        return null;
      }

      @Override public CompletableFuture<NarrowcastProgressResponse> getNarrowcastProgress(String requestId) {
        return null;
      }

      @Override public CompletableFuture<MessageQuotaResponse> getMessageQuota() {
        return null;
      }

      @Override public CompletableFuture<QuotaConsumptionResponse> getMessageQuotaConsumption() {
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

      @Override public CompletableFuture<NumberOfMessagesResponse> getNumberOfSentBroadcastMessages(String date) {
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

      @Override public CompletableFuture<GroupSummaryResponse> getGroupSummary(String groupId) {
        return null;
      }

      @Override public CompletableFuture<GroupMemberCountResponse> getGroupMemberCount(String groupId) {
        return null;
      }

      @Override public CompletableFuture<RoomMemberCountResponse> getRoomMemberCount(String roomId) {
        return null;
      }

      @Override
      public CompletableFuture<RichMenuResponse> getRichMenu(String richMenuId) {
        return null;
      }

      @Override public CompletableFuture<BotApiResponse> validateRichMenuObject(RichMenu richMenu) {
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

      @Override public CompletableFuture<BotApiResponse> createRichMenuAlias(CreateRichMenuAliasRequest request) {
        return null;
      }

      @Override public CompletableFuture<BotApiResponse> updateRichMenuAlias(String richMenuAliasId,
          UpdateRichMenuAliasRequest request) {
        return null;
      }

      @Override public CompletableFuture<RichMenuAliasResponse> getRichMenuAlias(String richMenuAliasId) {
        return null;
      }

      @Override public CompletableFuture<RichMenuAliasListResponse> getRichMenuAliasList() {
        return null;
      }

      @Override public CompletableFuture<BotApiResponse> deleteRichMenuAlias(String richMenuAliasId) {
        return null;
      }

      @Override
      public CompletableFuture<IssueLinkTokenResponse> issueLinkToken(String userId) {
        return null;
      }

      @Override public CompletableFuture<GetNumberOfMessageDeliveriesResponse> getNumberOfMessageDeliveries(String date) {
        return null;
      }

      @Override public CompletableFuture<GetNumberOfFollowersResponse> getNumberOfFollowers(String date) {
        return null;
      }

      @Override public CompletableFuture<GetFollowersResponse> getFollowers(GetFollowersRequest request) {
        return null;
      }

      @Override public CompletableFuture<GetMessageEventResponse> getMessageEvent(String requestId) {
        return null;
      }

      @Override public CompletableFuture<GetFriendsDemographicsResponse> getFriendsDemographics() {
        return null;
      }

      @Override public CompletableFuture<BotInfoResponse> getBotInfo() {
        return null;
      }

      @Override public CompletableFuture<GetWebhookEndpointResponse> getWebhookEndpoint() {
        return null;
      }

      @Override public CompletableFuture<SetWebhookEndpointResponse> setWebhookEndpoint(SetWebhookEndpointRequest request) {
        return null;
      }

      @Override public CompletableFuture<TestWebhookEndpointResponse> testWebhookEndpoint(
          TestWebhookEndpointRequest request) {
        return null;
      }

      @Override public CompletableFuture<GetStatisticsPerUnitResponse> getStatisticsPerUnit(String customAggregationUnit,
          String from, String to) {
        return null;
      }

      @Override public CompletableFuture<GetAggregationUnitUsageResponse> getAggregationUnitUsage() {
        return null;
      }

      @Override public CompletableFuture<GetAggregationUnitNameListResponse> getAggregationUnitNameList(String limit,
          String start) {
        return null;
      }
    };
  }

}
