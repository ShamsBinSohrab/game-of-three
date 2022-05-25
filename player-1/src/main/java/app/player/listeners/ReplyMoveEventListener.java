package app.player.listeners;

import app.player.domains.Move;
import app.player.events.DeleteQueueEvent;
import app.player.events.LogReceivedMoveEvent;
import app.player.events.LogSentMoveEvent;
import app.player.events.ReplyMoveEvent;
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
public class ReplyMoveEventListener {

  private final String outgoingQueue;
  private final RabbitTemplate rabbitTemplate;
  private final ApplicationEventPublisher applicationEventPublisher;

  public ReplyMoveEventListener(
      RabbitTemplate rabbitTemplate,
      ApplicationEventPublisher applicationEventPublisher,
      @Value("${game.queue.name}") String outgoingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.applicationEventPublisher = applicationEventPublisher;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doReply(ReplyMoveEvent event) {
    var move = (Move) event.getSource();
    var incomingQueue = event.getIncomingQueue();
    var receivedCorrelationId = event.getCorrelationId();
    applicationEventPublisher.publishEvent(new LogReceivedMoveEvent(move, receivedCorrelationId));

    var nextMove = move.newMove();
    var correlationId = UUID.randomUUID();
    if (nextMove.didIWin()) {
      log.debug("I won");
      rabbitTemplate.convertAndSend(
          outgoingQueue,
          nextMove.checkmate(),
          msg -> {
            msg.getMessageProperties().setCorrelationId(correlationId.toString());
            return msg;
          });
      applicationEventPublisher.publishEvent(new DeleteQueueEvent(incomingQueue));
      return;
    }
    rabbitTemplate.convertAndSend(
        outgoingQueue, nextMove, messagePostProcessor(correlationId, incomingQueue));
    applicationEventPublisher.publishEvent(new LogSentMoveEvent(nextMove, receivedCorrelationId));
  }

  private MessagePostProcessor messagePostProcessor(UUID correlationId, String incomingQueue) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId.toString());
      message.getMessageProperties().setReplyTo(incomingQueue);
      return message;
    };
  }
}
