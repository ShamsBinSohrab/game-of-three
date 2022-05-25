package app.player.listeners;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import app.player.domains.Move;
import app.player.events.GameStartEvent;
import app.player.events.InitiateConsumerEvent;
import app.player.events.LogSentMoveEvent;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
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
    var gameId = (UUID) event.getSource();
    var userInput = event.isUserInput();
    var move = Move.initialMove(gameId, userInput);
    var incomingQueue = randomAlphanumeric(10);
    var correlationId = UUID.randomUUID();
    applicationEventPublisher.publishEvent(new InitiateConsumerEvent(incomingQueue));
    rabbitTemplate.convertAndSend(
        outgoingQueue, move, messagePostProcessor(correlationId, incomingQueue));
    applicationEventPublisher.publishEvent(new LogSentMoveEvent(move, correlationId));
  }

  private MessagePostProcessor messagePostProcessor(UUID correlationId, String incomingQueue) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId.toString());
      message.getMessageProperties().setReplyTo(incomingQueue);
      return message;
    };
  }
}
