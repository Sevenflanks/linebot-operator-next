package next.operator.config.linebot;

import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LineBotServerInterceptor extends com.linecorp.bot.spring.boot.interceptor.LineBotServerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (handler instanceof HandlerMethod) {
      return super.preHandle(request, response, handler);
    } else {
      return true;
    }
  }
}
