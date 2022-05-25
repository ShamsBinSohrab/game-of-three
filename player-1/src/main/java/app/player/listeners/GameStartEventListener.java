package app.player.listeners;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.RandomUtils.nextInt;

import app.player.domains.Move;
import app.player.events.EventFactory;
import app.player.events.GameStartEvent;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GameStartEventListener {

  private final String outgoingQueue;
  private final EventFactory eventFactory;
  private final RabbitTemplate rabbitTemplate;

  public GameStartEventListener(
      EventFactory eventFactory,
      RabbitTemplate rabbitTemplate,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.eventFactory = eventFactory;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doStartGame(GameStartEvent event) {
    var gameId = (UUID) event.getSource();
    if (event.isUserInput()) {
      // TODO take user input
    }
    var number = nextInt();
    var move = Move.initialMove(gameId, number);
    var incomingQueue = randomAlphanumeric(10);
    var correlationId = UUID.randomUUID();
    eventFactory.initiateConsumer(incomingQueue);
    rabbitTemplate.convertAndSend(
        outgoingQueue, move, messagePostProcessor(correlationId, incomingQueue));
    eventFactory.logSentMove(move, correlationId);
  }

  private MessagePostProcessor messagePostProcessor(UUID correlationId, String incomingQueue) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId.toString());
      message.getMessageProperties().setReplyTo(incomingQueue);
      return message;
    };
  }
}
