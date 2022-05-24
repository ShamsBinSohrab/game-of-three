package app.player.listeners;

import app.player.domains.Move;
import app.player.events.ReplyMoveEvent;
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
public class ReplyMoveEventListener {

  private final RabbitTemplate rabbitTemplate;
  private final String outgoingQueue;

  public ReplyMoveEventListener(
      RabbitTemplate rabbitTemplate, @Value("${game.queue.name}") String outgoingQueue) {
    this.rabbitTemplate = rabbitTemplate;
    this.outgoingQueue = outgoingQueue;
  }

  @Async
  @EventListener
  public void doReply(ReplyMoveEvent event) {
    var move = (Move) event.getSource();
    var incomingQueue = event.getIncomingQueue();
    var nextMove = move.newMove();
    if (nextMove.didIWin()) {
      log.debug("I won");
    } else {
      var correlationId = UUID.randomUUID();
      log.debug("Sending message: {}, move: {}", correlationId, nextMove);
      rabbitTemplate.convertAndSend(
          outgoingQueue, nextMove, messagePostProcessor(correlationId, incomingQueue));
    }
  }

  private MessagePostProcessor messagePostProcessor(UUID correlationId, String incomingQueue) {
    return message -> {
      message.getMessageProperties().setCorrelationId(correlationId.toString());
      message.getMessageProperties().setReplyTo(incomingQueue);
      return message;
    };
  }
}
