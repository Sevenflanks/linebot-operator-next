package next.operator.linebot.talker;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import next.operator.linebot.service.RespondentTalkable;
import next.operator.utils.NumberUtils;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubTalker implements RespondentTalkable {
    Pattern REF_DAYS = Pattern.compile("([0-9一二三四五六七八九十明後]+)天");
    Pattern REF_DATE = Pattern.compile("(?:([0-9一二三四五六七八九十明後]+)年)?(?:([0-9一二三四五六七八九十下個]+)月)?(?:([0-9一二三四五六七八九十]+)[日號])?");

    @Override
    public boolean isReadable(String message) {
        return message.contains("提醒") && (message.contains(":") || message.contains("："));
    }

    @Override
    public Consumer<MessageEvent<TextMessageContent>> doFirst(LineMessagingClient client) {
        return null;
    }

    @Override
    public String talk(String message) {
        Matcher refDaysMatch = REF_DAYS.matcher(message);
        LocalDate date;
        if (refDaysMatch.matches()) {
            String refDaysTx = refDaysMatch.group(1);
            boolean isAllNum = refDaysTx.matches("^[0-9]+$");// 從頭到尾都是數字
            if (isAllNum) {

            }
        }


        return null;
    }



}
