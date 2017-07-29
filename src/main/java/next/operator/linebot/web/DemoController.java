package next.operator.linebot.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

  @RequestMapping("/helloworld/{message}")
  public String helloworld(@PathVariable String message) {
    return "HelloWorld: " + message;
  }

}