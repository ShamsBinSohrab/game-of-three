package app.player.listeners;

import app.player.domains.Move;
import app.player.events.GameStartEvent;
import app.player.events.InitQueueEvent;
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
public class GameStartEventListener {

  private final RabbitTemplate rabbitTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final String outgoingQueue;

  public GameStartEventListener(
      RabbitTemplate rabbitTemplate,
      ApplicationEventPublisher applicationEventPublisher,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.applicationEventPublisher = applicationEventPublisher;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doStartGame(GameStartEvent event) {
    var move = (Move) event.getSource();
    var correlationId = UUID.randomUUID().toString();
    var initQueueEvent = new InitQueueEvent(event.getReplyToQueue());
    applicationEventPublisher.publishEvent(initQueueEvent);
    rabbitTemplate.convertAndSend(
        outgoingQueue,
        move,
        msg -> {
          msg.getMessageProperties().setCorrelationId(correlationId);
          msg.getMessageProperties().setReplyTo(event.getReplyToQueue());
          return msg;
        });
    log.debug("Message {}, initial move: {}", correlationId, move);
  }
}
