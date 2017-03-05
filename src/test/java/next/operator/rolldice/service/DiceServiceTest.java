package next.operator.rolldice.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import next.operator.GenericTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class DiceServiceTest extends GenericTest {

    @Autowired
    private DiceService diceService;

    @Test
    public void roll1() throws Exception {
        log.info("{}", Lists.newArrayList(diceService.roll(100, 6)));
    }

    @Test
    public void roll2() throws Exception {
        log.info("{}", Lists.newArrayList(diceService.roll(new Integer[]{6, 6, 6, 6, 6, 6})));
    }

}