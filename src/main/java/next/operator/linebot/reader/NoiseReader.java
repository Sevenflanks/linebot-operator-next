package next.operator.linebot.reader;

import next.operator.ChineseTokens;
import next.operator.linebot.service.RespondentReadable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 回垃圾話用的
 * 當對話中出現[要不要][去不去]等N不N文法時
 * 隨機回垃圾話: N or 不N or 想N
 */
@Service
public class NoiseReader implements RespondentReadable {

	@Autowired
	private ChineseTokens chineseTokens;

	final Pattern readPattern = Pattern.compile("([\\u4e00-\\u9fa5A-Za-z]+?)不(\\1[\\u4e00-\\u9fa5A-Za-z]*)");
	final String[] response = {"", "不"};

	@Override
	public boolean isReadable(String message) {
		return readPattern.matcher(message).find();
	}

	@Override
	public String read(String message) {
		final Matcher matcher = readPattern.matcher(message);
		if (matcher.find()) {
			final String keyWord = chineseTokens.run(matcher.group(2))[0];
			return response[(int)(Math.random() * response.length)] + keyWord;
		} else {
			return null;
		}
	}
}
