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
import com.linecorp.bot.model.profile.UserProfileResponse;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.CompletableFuture;

@Slf4j
@SpringBootApplication(
    exclude = {LineBotAutoConfiguration.class}
)
public class TestApp {

  public static void main(String[] args) {
    SpringApplication.run(TestApp.class, args);
  }

  @Bean
  @Primary
  public LineMessagingClient lineMessagingClient() {
    return new LineMessagingClient() {

      @Override
      public CompletableFuture<BotApiResponse> replyMessage(ReplyMessage replyMessage) {
        System.out.println("replyMessage:" + replyMessage);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<BotApiResponse> pushMessage(PushMessage pushMessage) {
        System.out.println("pushMessage:" + pushMessage);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<BotApiResponse> multicast(Multicast multicast) {
        System.out.println("multicast:" + multicast);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<MessageContentResponse> getMessageContent(String messageId) {
        System.out.println("getMessageContent:" + messageId);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<UserProfileResponse> getProfile(String userId) {
        System.out.println("getProfile:" + userId);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<BotApiResponse> leaveGroup(String groupId) {
        System.out.println("leaveGroup:" + groupId);
        return CompletableFuture.supplyAsync(() -> null);
      }

      @Override
      public CompletableFuture<BotApiResponse> leaveRoom(String roomId) {
        System.out.println("leaveRoom:" + roomId);
        return CompletableFuture.supplyAsync(() -> null);
      }
    };
  }

}
