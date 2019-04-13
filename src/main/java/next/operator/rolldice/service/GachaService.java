package next.operator.rolldice.service;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SplittableRandom;

/**
 * 轉蛋功能
 */
@Slf4j
@Service
public class GachaService {

  public static final String 銀 = "[銀]";
  public static final String 金 = "[金]";
  public static final String 彩 = "[彩]";
  public static final String 蘿 = "[蘿]";

  // 通常機率
  final List<Unit> PCRD_UNITS = Lists.newArrayList(
      new Unit(蘿,      1),
      new Unit(彩,  1_999),
      new Unit(金, 18_000),
      new Unit(銀, 80_000));

  // 保底機率
  final List<Unit> PCRD_UNITS_GUARANTEE = Lists.newArrayList(
      new Unit(蘿,      1),
      new Unit(彩,  1_999),
      new Unit(金, 98_000));

  private final SplittableRandom random;

  public GachaService() throws NoSuchAlgorithmException {
    this.random = new SplittableRandom();
  }

  public Unit singlePumping() {
    return this.singlePumping(PCRD_UNITS);
  }
  public Unit guaranteePumping() {
    return this.singlePumping(PCRD_UNITS_GUARANTEE);
  }
  public Unit singlePumping(List<Unit> units) {
    // 本次抽出
    final int e = random.nextInt(100_000);

    int lastSpacing = 0;
    for (Unit unit : units) {
      if (e >= lastSpacing && e < (lastSpacing += unit.percentage)) {
        return unit;
      }
    }

    throw new RuntimeException("機率有問題，趕快下線維修！");
  }

  public List<Unit> tenPumping() {
    return this.tenPumping(PCRD_UNITS, PCRD_UNITS_GUARANTEE);
  }
  public List<Unit> tenPumping(List<Unit> units, List<Unit> guarantee) {
    return Lists.newArrayList(
        singlePumping(units), // 1
        singlePumping(units), // 2
        singlePumping(units), // 3
        singlePumping(units), // 4
        singlePumping(units), // 5
        singlePumping(units), // 6
        singlePumping(units), // 7
        singlePumping(units), // 8
        singlePumping(units), // 9
        singlePumping(guarantee) // 10
    );
  }

  @Getter
  @AllArgsConstructor
  @ToString
  public static class Unit {
    /** 顯示名稱 */
    private String name;

    /** 排出機率 1000 = 1% */
    private int percentage;
  }

}
