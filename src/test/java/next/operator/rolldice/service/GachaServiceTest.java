package next.operator.rolldice.service;

import lombok.extern.slf4j.Slf4j;
import next.operator.GenericTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Slf4j
public class GachaServiceTest extends GenericTest {

  @Autowired GachaService gachaService;

  @Test
  public void test() {
    System.out.println(gachaService.tenPumping(gachaService.PCRD_UNITS, gachaService.PCRD_UNITS_GUARANTEE).stream()
        .map(GachaService.Unit::getName)
        .collect(Collectors.joining()));

    System.out.println(gachaService.tenPumping(gachaService.PCRD_UNITS, gachaService.PCRD_UNITS_GUARANTEE).stream()
        .map(GachaService.Unit::getName)
        .collect(Collectors.joining()));

    System.out.println(gachaService.tenPumping(gachaService.PCRD_UNITS, gachaService.PCRD_UNITS_GUARANTEE).stream()
        .map(GachaService.Unit::getName)
        .collect(Collectors.joining()));

    System.out.println(gachaService.tenPumping(gachaService.PCRD_UNITS, gachaService.PCRD_UNITS_GUARANTEE).stream()
        .map(GachaService.Unit::getName)
        .collect(Collectors.joining()));

    System.out.println(gachaService.tenPumping(gachaService.PCRD_UNITS, gachaService.PCRD_UNITS_GUARANTEE).stream()
        .map(GachaService.Unit::getName)
        .collect(Collectors.joining()));
  }

}
