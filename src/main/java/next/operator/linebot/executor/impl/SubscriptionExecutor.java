package next.operator.linebot.executor.impl;

import com.google.common.collect.Lists;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.common.uncheck.Uncheck;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.scheduled.SubscriptionJob;
import next.operator.subscription.dao.SubscriptionDao;
import next.operator.subscription.entity.Subscription;
import next.operator.subscription.service.SubscriptionService;
import next.operator.user.dao.UserDao;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubscriptionExecutor implements FunctionExecutable {

  @Autowired
  private SubscriptionDao subscriptionDao;

  @Autowired
  private SubscriptionService subscriptionService;

  @Autowired
  private SubscriptionJob subscriptionJob;

  @Autowired
  private UserDao userDao;

  static final List<String> ORDER_DELETE = Lists.newArrayList("delete", "刪");
  static final List<String> ORDER_DELETE_ALL = Lists.newArrayList("delete_all", "刪全部");

  private static final Options options;
  private static final CommandLineParser parser = new DefaultParser();

  static {
    options = new Options();

    options.addOption(Option.builder("l")
        .longOpt("查")
        .hasArg(false)
        .required(false)
        .desc("查詢自己的所有訂閱")
        .build());

    options.addOption(Option.builder("s")
        .longOpt("文")
        .hasArg(true)
        .numberOfArgs(1)
        .optionalArg(false)
        .required(false)
        .desc("設定訂閱的內容，為純文字內容，若內容中含有空白則以\"\"包含")
        .build());

    options.addOption(Option.builder("t")
        .longOpt("時")
        .hasArg(true)
        .numberOfArgs(1)
        .optionalArg(false)
        .required(false)
        .desc("設定時間點，格式為yyyy-MM-ddTHH:mm")
        .build());

    options.addOption(Option.builder("d")
        .longOpt("隔")
        .hasArg(true)
        .numberOfArgs(1)
        .optionalArg(false)
        .required(false)
        .desc("設定時間間隔，為秒數")
        .build());

    options.addOption(Option.builder("i")
        .longOpt("略")
        .hasArg(true)
        .numberOfArgs(2)
        .optionalArg(false)
        .required(false)
        .desc("設定不出現此訂閱消息得時間，有兩個時間參數，需與空白間隔")
        .build());

    options.addOption(Option.builder("r")
        .longOpt("刪")
        .hasArg(true)
        .numberOfArgs(1)
        .optionalArg(true)
        .required(false)
        .desc("刪除訂閱，若輸入數字則為刪除特定ID的訂閱，若輸入ALL則為刪除所有自己的訂閱")
        .build());
  }

  @Override
  public String[] structures() {
    return new String[]{
        "/sub",
        "！訂"
    };
  }

  @Override
  public String execute(MessageEvent<TextMessageContent> event, String... args) {
    if (args.length <= 0) {
      return doFindSubcriped(event);
    } else if (args.length <= 2) {
      return doFunction(event, args);
    } else {
      return doSubscriping(event, args);
    }
  }

  private String doSubscriping(MessageEvent<TextMessageContent> event, String[] args) {
    final Instant startTime = tryArg(args, 0, Uncheck.apply(s -> LocalDateTime.parse(s).toInstant(ZoneOffset.ofHours(8))))
          .orElseThrow(() -> new ValidationException("開始時間的參數好像有問題歐，格式是 yyyy-MM-ddTHH:mm"));
    final Duration fixRate = tryArg(args, 1, Uncheck.apply(s -> Duration.ofSeconds(Integer.parseInt(s))))
        .orElseThrow(() -> new ValidationException("時間間隔的參數好像有問題歐，必須要是一個數字！"));
    final String msg = Stream.of(args).skip(2).collect(Collectors.joining(" "));

    final Subscription subscription = subscriptionService.subscribe(event.getSource(), startTime, fixRate, msg);
    subscriptionJob.subscribe(subscription);

    return userDao.getCurrentUserName() + "你的訊息訂閱成功了喔！";
  }

  private String doFunction(MessageEvent<TextMessageContent> event, String[] args) {
    final String order = args[0];

    if (ORDER_DELETE.contains(order)) {
      final Long id = tryArg(args, 1, Uncheck.apply(Long::parseLong))
          .orElseThrow(() -> new ValidationException("需要第二個參數喔，而且要是個數字"));
      subscriptionService.deSubscribe(event.getSource(), id);
      return userDao.getCurrentUserName() + "已經把你其中一個訂閱取消了！";
    } else if (ORDER_DELETE_ALL.contains(order)) {
      subscriptionService.deSubscribe(event.getSource());
      return userDao.getCurrentUserName() + "已經把你的訂閱全部取消了！";
    } else {
      return "看不懂【" + order + "】是想要做什麼喔，是不是有打錯字？";
    }

  }

  private String doFindSubcriped(MessageEvent<TextMessageContent> event) {
    final List<Subscription> dbSubscriptions = subscriptionDao.findBySubscriber_SubscriberId(event.getSource().getUserId());
    if (dbSubscriptions.isEmpty()) {
      return userDao.getCurrentUserName() + "你目前沒有訂閱任何訊息喔";
    } else {
      return userDao.getCurrentUserName() + "你目前訂閱了" + dbSubscriptions.size() + "個訊息:\n" +
          dbSubscriptions.stream().map(Subscription::toString).collect(Collectors.joining("\n"));
    }
  }

  private <T> Optional<T> tryArg(String[] args, int idx, Function<String, T> mapper) {
    try {
      if (args.length >= idx) {
        return Optional.ofNullable(mapper.apply(args[idx]));
      } else {
        return Optional.empty();
      }
    } catch (Exception e) {
      return Optional.empty();
    }
  }

}
