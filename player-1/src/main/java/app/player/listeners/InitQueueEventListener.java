package app.player.listeners;

import static org.springframework.util.SerializationUtils.deserialize;

import app.player.domains.Move;
import app.player.events.InitiateConsumerEvent;
import app.player.events.ReplyMoveEvent;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitQueueEventListener {

  private final Channel channel;
  private final String outgoingQueue;
  private final RabbitTemplate rabbitTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;

  public InitQueueEventListener(
      Channel channel,
      RabbitTemplate rabbitTemplate,
      ApplicationEventPublisher applicationEventPublisher,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.channel = channel;
    this.rabbitTemplate = rabbitTemplate;
    this.applicationEventPublisher = applicationEventPublisher;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doInitQueues(InitiateConsumerEvent event) throws IOException {
    var incomingQueue = (String) event.getSource();
    channel.queueDeclare(incomingQueue, false, false, true, Collections.emptyMap());
    channel.basicConsume(incomingQueue, deliverCallback(incomingQueue), cancelCallback());
  }

  private DeliverCallback deliverCallback(String incomingQueue) {
    return (tag, message) -> {
      var move = (Move) deserialize(message.getBody());
      var correlationId = UUID.fromString(message.getProperties().getCorrelationId());
      applicationEventPublisher.publishEvent(
          new ReplyMoveEvent(move, incomingQueue, correlationId));
    };
  }

  private CancelCallback cancelCallback() {
    return (tag) -> log.debug("Cancelled: " + tag);
  }
}
