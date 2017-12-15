package next.operator.currency.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CurrencyType {

  USD(Arrays.asList("美元", "美金", "鎂")),
  GBP(Arrays.asList("英鎊")),
  EUR(Arrays.asList("歐元")),
  AUD(Arrays.asList("澳元", "澳幣")),
  NZD(Arrays.asList("紐元")),
  HKD(Arrays.asList("港幣")),
  SGD(Arrays.asList("星幣")),
  JPY(Arrays.asList("日幣", "日圓", "日元")),
  CNY(Arrays.asList("人民幣")),
  MYR(Arrays.asList("馬來幣")),
  NLG(Arrays.asList("荷蘭幣")),
  CAD(Arrays.asList("加幣", "加元")),
  BEF(Arrays.asList("比利時法郎")),
  ZAR(Arrays.asList("南非幣")),
  DKK(Arrays.asList("丹麥幣")),
  SEK(Arrays.asList("瑞典克郎")),
  CHF(Arrays.asList("瑞士法郎")),
  FIM(Arrays.asList("芬蘭幣")),
  NOK(Arrays.asList("挪威幣")),
  KRW(Arrays.asList("韓圜", "韓幣", "韓元")),
  THB(Arrays.asList("泰銖", "泰幣")),
  IDR(Arrays.asList("印尼幣")),
  PHP(Arrays.asList("菲幣")),
  ATS(Arrays.asList("奧先令")),
  SAR(Arrays.asList("沙幣")),
  INR(Arrays.asList("印度幣")),
  IEP(Arrays.asList("愛爾蘭幣")),
  MOP(Arrays.asList("澳門幣")),
  MXN(Arrays.asList("墨西哥幣")),
  PLN(Arrays.asList("波蘭幣")),
  TWD(Arrays.asList("台幣"));


  private static final Map<String, CurrencyType> MAP_BY_CODE = Stream.of(CurrencyType.values()).collect(Collectors.toMap(CurrencyType::name, Function.identity()));
  private static final Map<String, CurrencyType> MAP_BY_LOCALNAME = Stream.of(CurrencyType.values())
      .flatMap(ct -> ct.localName.stream().collect(Collectors.toMap(Function.identity(), ln -> ct)).entrySet().stream())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

  public static Optional<CurrencyType> tryParse(String code) {
    return Optional.ofNullable(MAP_BY_CODE.get(code));
  }

  public static Optional<CurrencyType> tryParseByName(String name) {
    return Optional.ofNullable(MAP_BY_LOCALNAME.get(name));
  }

  private List<String> localName;

  public List<String> getLocalName() {
    return localName;
  }

  public String getFirstLocalName() {
    return localName.get(0);
  }

  CurrencyType(List<String> localName) {
    this.localName = localName;
  }
}
