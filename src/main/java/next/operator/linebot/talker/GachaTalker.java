package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.service.RespondentTalkable;
import next.operator.rolldice.service.GachaService;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class GachaTalker implements RespondentTalkable {

  @Autowired
  private UserDao userDao;

  @Autowired
  private GachaService gachaService;

  @Override
  public boolean isReadable(String message) {
    return message.contains("曬卡");
  }

  @Override
  public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
    return null;
  }

  @Override
  public String talk(String message) {

    final List<GachaService.Unit> results = gachaService.tenPumping();

    final StringBuilder sb = new StringBuilder();
    sb.append(userDao.getCurrentUserName()).append("\n");

    int goldCnt = 0;
    for (int i = 0; i < results.size(); i++) {
      final GachaService.Unit unit = results.get(i);

      // 計算是否為保底
      if (unit.getName().equals(GachaService.金)) {
        goldCnt++;
      }

      sb.append(unit.getName());

      // 每五個換一行
      if (i % 5 == 4 && i < results.size() - 1) {
        sb.append("\n");
      }
    }

    if (goldCnt == 1) {
      sb.append("(保底)");
    }

    return sb.toString();
  }
}
