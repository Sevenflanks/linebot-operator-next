package next.operator.linebot.talker;

import next.operator.ChineseTokens;
import next.operator.GenericTest;
import next.operator.linebot.service.RespondentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;

public class NoiseTalkerTest extends GenericTest {

	@Autowired
	private RespondentService respondentService;

	@Autowired
	private ChineseTokens chineseTokens;

	@Test
	public void test() throws IOException, ClassNotFoundException {
//		for (int i = 0; i < 100; i++) {
//			System.out.println(respondentService.response("這句話懂不懂"));
//			System.out.println(respondentService.response("到底奇不奇怪"));
//			System.out.println(respondentService.response("low不low"));
//			System.out.println(respondentService.response("奇怪不奇怪哈哈"));
//			System.out.println(respondentService.response("神不神奇顆顆"));
//			System.out.println(respondentService.response("30歲前能不能結婚"));
//			System.out.println(respondentService.response("只是不知道造不造的到（？"));
//		}
		System.out.println(Arrays.toString(chineseTokens.run("這句話懂不懂")));
		System.out.println(Arrays.toString(chineseTokens.run("到底奇不奇怪")));
		System.out.println(Arrays.toString(chineseTokens.run("low不low")));
		System.out.println(Arrays.toString(chineseTokens.run("奇怪不奇怪哈哈")));
		System.out.println(Arrays.toString(chineseTokens.run("奇怪哈")));
		System.out.println(Arrays.toString(chineseTokens.run("神不神奇顆顆")));
		System.out.println(Arrays.toString(chineseTokens.run("30歲前能不能結婚")));
		System.out.println(Arrays.toString(chineseTokens.run("只是不知道造不造的到（？")));
		System.out.println(Arrays.toString(chineseTokens.run("買skin")));
		System.out.println(Arrays.toString(chineseTokens.run("貓是不是蘿莉控")));
		System.out.println(Arrays.toString(chineseTokens.run("蘿莉不蘿莉是貓")));

	}

}