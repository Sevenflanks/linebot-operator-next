package next.operator.linebot.web;

import next.operator.linebot.handler.LineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineController {

    @Autowired
    private LineHandler lineHandler;

    @RequestMapping("/helloworld/{message}")
    public String helloworld(@PathVariable String message) {
        lineHandler.sendToAll(message);
        return "HelloWorld: " + message;
    }

}