package app.player.listeners;

import static org.springframework.util.SerializationUtils.deserialize;

import app.player.domains.GameInitRequest;
import app.player.domains.Move;
import app.player.events.GameStartEvent;
import app.player.events.InitQueueEvent;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitQueueEventListener {

  private final Channel channel;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void doInitQueues(InitQueueEvent event) throws IOException {
    var request = (GameInitRequest) event.getSource();
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.basicConsume(
        request.incomingQueue(), deliverCallback(request.incomingQueue()), cancelCallback());
    applicationEventPublisher.publishEvent(new GameStartEvent(request));
  }

  private DeliverCallback deliverCallback(String incoming) {
    return (tag, message) -> {
      var currentMove = (Move) deserialize(message.getBody());
      var nextMove = currentMove.newMove();
      var correlationId = UUID.randomUUID().toString();
      log.debug(
          "Received {}, id: {} <=> Sending {}, id: {}",
          currentMove.number(),
          message.getProperties().getCorrelationId(),
          nextMove.number(),
          correlationId);
      sleep(2);
      rabbitTemplate.convertAndSend(
          message.getProperties().getReplyTo(),
          nextMove,
          msg -> {
            msg.getMessageProperties().setCorrelationId(correlationId);
            msg.getMessageProperties().setReplyTo(incoming);
            return msg;
          });
    };
  }

  private CancelCallback cancelCallback() {
    return (tag) -> log.debug("Cancelled: " + tag);
  }

  static void sleep(int time) {
    try {
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
