package next.operator.linebot.executor.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.executor.FunctionExecutable;
import next.operator.rolldice.model.DiceModel;
import next.operator.rolldice.service.DiceService;
import next.operator.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class RollingDiceExecutor implements FunctionExecutable {

  @Autowired
  private DiceService diceService;

  @Autowired
  private UserDao userDao;

  @Override
  public String[] structures() {
    return new String[]{
        "/roll 骰數d骰面 [骰數d骰面...]",
        "！骰 骰數d骰面 [骰數d骰面...]"
    };
  }

  @Override
  public String execute(MessageEvent<TextMessageContent> event, String... args) {
    StringBuilder sb = new StringBuilder();
    sb.append(userDao.getCurrentUserName()).append("正在執骰\n");

    List<Integer[]> allResult = Lists.newArrayList();
    for (String rollDice : args) {
      final DiceModel dice = parse(rollDice);
      final Integer[] rollResult = diceService.roll(dice);
      allResult.add(rollResult);
      sb.append("丟了").append(dice.getTimes()).append("顆").append(dice.getMax()).append("面骰");
      Optional.ofNullable(Strings.emptyToNull(dice.getCorrection())).ifPresent(c -> sb.append("，加權(").append(c).append(")"));
      sb.append("：\n");
      sb.append("\t[").append(Stream.of(rollResult).map(String::valueOf).collect(Collectors.joining("+"))).append("]\n");
    }
    if (allResult.isEmpty()) {
      sb.append("...欸？你沒有丟骰子啊！");
    } else {
      sb.append("總合=").append(allResult.stream().flatMap(Stream::of).mapToInt(i -> i).sum());
    }

    return sb.toString();
  }

  private DiceModel parse(String rollDice) {
    rollDice = rollDice.toLowerCase();
    if (rollDice.contains("d")) {
      final int[] result = new int[2];
      final String[] ds = rollDice.split("d");
      if (ds.length != 2) {
        throw new ValidationException(userDao.getCurrentUserName() + "你的這顆骰子【" + rollDice + "】不太對勁喔！");
      }

      final DiceModel diceModel = new DiceModel();
      Integer times = Ints.tryParse(ds[0]);
      checkNum(ds[0], times);
      diceModel.setTimes(times);

      Pattern numPattern = Pattern.compile("^(\\d+)(.*)");
      final Matcher matcher = numPattern.matcher(ds[1]);
      if (!matcher.matches()) {
        throw new ValidationException(userDao.getCurrentUserName() + "你的這顆骰子【" + rollDice + "】不太對勁喔！");
      } else if (matcher.groupCount() > 1) {
        diceModel.setCorrection(matcher.group(2));
      }
      Integer max = Ints.tryParse(matcher.group(1));
      checkNum(ds[1], max);
      diceModel.setMax(max);

      return diceModel;
    } else {
      throw new ValidationException(userDao.getCurrentUserName() + "你的這顆骰子【" + rollDice + "】不太對勁喔！");
    }
  }

  private void checkNum(String origin, Integer num) {
    if (num == null) {
      throw new ValidationException(userDao.getCurrentUserName() + "你的這數字【" + origin + "】不太對勁喔！");
    } else if (num > 100) {
      throw new ValidationException(userDao.getCurrentUserName() + "如果你想要骰出100以上的骰子，請多多斗內我QQ");
    } else if (num <= 0) {
      throw new ValidationException(userDao.getCurrentUserName() + "擲骰結果是!@#$%^&*()");
    }
  }

}
