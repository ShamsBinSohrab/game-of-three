package app.player.listeners;

import static org.springframework.util.SerializationUtils.deserialize;

import app.player.domains.Move;
import app.player.events.InitiateConsumerEvent;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitQueueEventListener {

  private final Channel channel;
  private final RabbitTemplate rabbitTemplate;
  private final String outgoingQueue;

  public InitQueueEventListener(
      Channel channel,
      RabbitTemplate rabbitTemplate,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.channel = channel;
    this.rabbitTemplate = rabbitTemplate;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doInitQueues(InitiateConsumerEvent event) throws IOException {
    var queue = (String) event.getSource();
    channel.queueDeclare(queue, false, false, true, Collections.emptyMap());
    channel.basicConsume(queue, deliverCallback(queue), cancelCallback());
  }

  private DeliverCallback deliverCallback(String queue) {
    return (tag, message) -> {
      var opponentMove = (Move) deserialize(message.getBody());
      log.debug(
          "Received message: {}, move: {}",
          message.getProperties().getCorrelationId(),
          opponentMove);
      var nextMove = opponentMove.newMove();
      if (nextMove.didIWin()) {
        log.debug("I won");
      } else {
        var correlationId = UUID.randomUUID().toString();
        sleep(2);
        log.debug("Sending message: {}, move: {}", correlationId, nextMove);
        rabbitTemplate.convertAndSend(
            outgoingQueue,
            nextMove,
            msg -> {
              msg.getMessageProperties().setCorrelationId(correlationId);
              msg.getMessageProperties().setReplyTo(queue);
              return msg;
            });
      }
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
