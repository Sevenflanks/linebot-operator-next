package next.operator.linebot.executor.impl;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.scheduled.SubscriptionJob;
import next.operator.subscription.entity.Subscription;
import next.operator.subscription.service.SubscriptionService;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubscriptionExecutor implements FunctionExecutable {

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private SubscriptionJob subscriptionJob;

  @Autowired
  private UserDao userDao;

  @Override
  public String[] structures() {
    return new String[]{
        "/sub",
        "！訂"
    };
  }

  @Override
  public String execute(MessageEvent<TextMessageContent> event, String... args) {
    if (args.length < 3) {
      subscriptionService.deSubscribe(event.getSource());
      return userDao.getCurrentUserName() + "已經把你的訂閱取消了！";
    } else {
      final Instant startTime;
      try {
        startTime = LocalDateTime.parse(args[0]).toInstant(ZoneOffset.ofHours(8));
      } catch (Exception e) {
        throw new ValidationException("開始時間的參數好像有問題歐，格式是 yyyy-MM-ddTHH:mm");
      }
      final Duration fixRate;
      try {
        fixRate = Duration.ofSeconds(Integer.parseInt(args[1]));
      } catch (NumberFormatException e) {
        throw new ValidationException("時間間隔的參數好像有問題歐，必須要是一個數字！");
      }
      final String msg = Stream.of(args).skip(2).collect(Collectors.joining(" "));

      final Subscription subscription = subscriptionService.subscribe(event.getSource(), startTime, fixRate, msg);
      subscriptionJob.subscribe(subscription);

      return userDao.getCurrentUserName() + "你的訊息訂閱成功了喔！";
    }
  }

}
