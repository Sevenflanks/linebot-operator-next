package next.operator.linebot.web;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.ReplyEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.parser.WebhookParser;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import next.operator.linebot.handler.EventConsumer;
import next.operator.linebot.handler.LineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 此處的職責等價於原本SDK中的 com.linecorp.bot.spring.boot.support.LineMessageHandlerSupport
 * 是一個用來接收 POST /callback 並交給EventHandler進行處理的 Controller
 */
@Slf4j
@RestController
public class LineController {

  private final WebhookParser parser;
  private final List<HandlerMethod> consumers;

  @Autowired
  private ReplyByReturnValueConsumer.Factory returnValueConsumerFactory;

  @Autowired
  private LineHandler lineHandler;

  public LineController(@NonNull LineSignatureValidator lineSignatureValidator) {
    this.parser = new WebhookParser(lineSignatureValidator);
    this.consumers = Arrays.stream(ReflectionUtils.getUniqueDeclaredMethods(lineHandler.getClass()))
        .peek(method -> Preconditions.checkState(method.getParameterCount() == 1,
            "Number of parameter should be 1. But {}",
            (Object[]) method.getParameterTypes()))
        .map(method -> {
          final EventConsumer eventConsumer = AnnotatedElementUtils.getMergedAnnotation(method, EventConsumer.class);
          final Type type = method.getGenericParameterTypes()[0];
          final Predicate<Event> predicate = new EventPredicate(type);
          return new HandlerMethod(predicate, lineHandler, method, eventConsumer.priority());
        })
        .sorted(Comparator.comparing(HandlerMethod::getPriority).reversed()) // 數字越大越優先
        .collect(Collectors.toList());
  }

  @SneakyThrows
  @PostMapping("/callback")
  public void callback(HttpServletRequest req) {
    // validate signature
    final String signature = req.getHeader("X-Line-Signature");
    final byte[] json = ByteStreams.toByteArray(req.getInputStream());
    CallbackRequest callbackRequest = parser.handle(signature, json);

    callbackRequest.getEvents().forEach(this::dispatchInternal);
  }

  @SneakyThrows
  private void dispatchInternal(final Event event) {
    final HandlerMethod handlerMethod = consumers
        .stream()
        .filter(consumer -> consumer.getSupportType().test(event))
        .findFirst()
        .orElseThrow(() -> new UnsupportedOperationException("Unsupported event type. " + event));
    final Object returnValue = handlerMethod.getHandler().invoke(handlerMethod.getObject(), event);

    if (returnValue != null) {
      returnValueConsumerFactory.createForEvent(event)
          .accept(returnValue);
    }
  }

  @Value
  static class HandlerMethod {
    Predicate<Event> supportType;
    Object object;
    Method handler;
    int priority;
  }

  /**
   * 挑選可以使用的consumer
   */
  private static class EventPredicate implements Predicate<Event> {
    private final Class<?> supportEvent;
    private final Class<? extends MessageContent> messageContentType;

    @SuppressWarnings("unchecked")
    EventPredicate(final Type mapping) {
      if (mapping == ReplyEvent.class) {
        supportEvent = ReplyEvent.class;
        messageContentType = null;
      } else if (mapping instanceof Class) {
        Preconditions.checkState(Event.class.isAssignableFrom((Class<?>) mapping),
            "Handler argument type should BE-A Event. But {}",
            mapping.getClass());
        supportEvent = (Class<? extends Event>) mapping;
        messageContentType = null;
      } else {
        final ParameterizedType parameterizedType = (ParameterizedType) mapping;
        supportEvent = (Class<? extends Event>) parameterizedType.getRawType();
        messageContentType =
            (Class<? extends MessageContent>)
                ((ParameterizedType) mapping).getActualTypeArguments()[0];
      }
    }

    @Override
    public boolean test(final Event event) {
      return supportEvent.isAssignableFrom(event.getClass())
          && (messageContentType == null
          || event instanceof MessageEvent
          && filterByType(messageContentType, ((MessageEvent<?>) event).getMessage()));
    }

    private static boolean filterByType(final Class<?> clazz, final Object content) {
      return clazz.isAssignableFrom(content.getClass());
    }

    @Override
    public String toString() {
      final StringBuilder sb = new StringBuilder();

      sb.append('[');
      if (messageContentType != null) {
        sb.append(MessageEvent.class.getSimpleName())
            .append('<')
            .append(messageContentType.getSimpleName())
            .append('>');
      } else {
        sb.append(supportEvent.getSimpleName());
      }
      sb.append(']');

      return sb.toString();
    }
  }

}
