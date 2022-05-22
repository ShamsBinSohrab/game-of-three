package app.player.listeners;

import app.player.events.HandshakeEvent;
import app.player.events.InitQueueEvent;
import app.player.game.GameInitRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HandshakeEventListener {

  private final RabbitTemplate rabbitTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final String queue;

  public HandshakeEventListener(
      RabbitTemplate rabbitTemplate,
      ApplicationEventPublisher applicationEventPublisher,
      @Value("${game.queue.name}") String queue) {
    this.rabbitTemplate = rabbitTemplate;
    this.applicationEventPublisher = applicationEventPublisher;
    this.queue = queue;
  }

  @Async
  @EventListener
  public void doHandshake(HandshakeEvent event) {
    var request = (GameInitRequest) event.getSource();
    rabbitTemplate.convertAndSend(queue, event.getSource());
    log.debug(
        "Initiating handshake request, incoming: {} and outgoing: {}",
        request.incomingQueue(),
        request.outgoingQueue());
    applicationEventPublisher.publishEvent(new InitQueueEvent(request));
  }
}
