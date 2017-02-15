package next.operator.linebot.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    @RequestMapping("/helloworld")
    public String helloworld() {
        return "helloworld";
    }

}
