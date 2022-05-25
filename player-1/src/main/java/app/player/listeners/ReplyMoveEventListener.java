package app.player.listeners;

import app.player.domains.Move;
import app.player.events.EventFactory;
import app.player.events.ReplyMoveEvent;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReplyMoveEventListener {

  private final String outgoingQueue;
  private final EventFactory eventFactory;
  private final RabbitTemplate rabbitTemplate;

  public ReplyMoveEventListener(
      EventFactory eventFactory,
      RabbitTemplate rabbitTemplate,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.eventFactory = eventFactory;
    this.outgoingQueue = outgoingQueue;
    this.rabbitTemplate = rabbitTemplate;
  }

  @Async
  @EventListener
  public void doReply(ReplyMoveEvent event) {
    var opponentMove = (Move) event.getSource();
    var incomingQueue = event.getIncomingQueue();
    var nextMove = opponentMove.nextMove();
    var correlationId = UUID.randomUUID();
    if (nextMove.didIWin()) {
      log.info("I won game: {}", nextMove.gameId());
      rabbitTemplate.convertAndSend(
          outgoingQueue, nextMove.checkmate(), postProcessor(correlationId, null));
      eventFactory.cancelConsumer(event.getTag());
      return;
    }
    rabbitTemplate.convertAndSend(
        outgoingQueue, nextMove, postProcessor(correlationId, incomingQueue));
    eventFactory.logSentMove(nextMove, correlationId);
  }

  private MessagePostProcessor postProcessor(UUID correlationId, @Nullable String incomingQueue) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId.toString());
      message.getMessageProperties().setReplyTo(incomingQueue);
      return message;
    };
  }
}
