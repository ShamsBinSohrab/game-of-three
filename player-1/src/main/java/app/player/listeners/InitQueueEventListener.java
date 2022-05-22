package app.player.listeners;

import app.player.events.GameStartEvent;
import app.player.events.InitQueueEvent;
import app.player.game.GameInitRequest;
import com.google.common.primitives.Longs;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
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
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    channel.basicConsume(request.incomingQueue(), deliverCallback(), cancelCallback());
    applicationEventPublisher.publishEvent(new GameStartEvent(request));
  }

  private DeliverCallback deliverCallback() {
    return (tag, message) -> {
      var number = Longs.fromByteArray(message.getBody());
      var routingKey = message.getEnvelope().getRoutingKey();
      log.debug("Received : {} from {}", number, message.getEnvelope().getRoutingKey());
      rabbitTemplate.convertAndSend(routingKey, Longs.toByteArray(RandomUtils.nextLong(1, 100)));
    };
  }

  private CancelCallback cancelCallback() {
    return (tag) -> log.debug("Cancelled: " + tag);
  }
}
