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
    final StringBuilder sb = new StringBuilder();
    sb.append("@").append(userDao.getCurrentUserName()).append("\n");

    int silverCnt = 0;
    final List<GachaService.Unit> units = gachaService.tenPumping();
    for (int i = 0; i < 10; i++) {
      final GachaService.Unit unit = units.get(i);
      sb.append(unit.getName());

      // 計算銀出現的次數，做為保底依據
      if (unit.getName().equals(GachaService.銀)) {
        silverCnt++;
      }

      // 每五個換一行
      // 第十個不換行
      if (i % 5 == 4 && i < 9) {
        sb.append("\n");
      }
    }

    if (silverCnt == 9) {
      sb.append("(保底)");
    }

    return sb.toString();
  }
}
