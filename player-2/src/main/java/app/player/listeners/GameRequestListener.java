package app.player.listeners;

import app.player.domains.Move;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "game-queue", concurrency = "5")
  void listenGameRequest(Move move, Message message) {
    if (move.didOpponentWin()) {
      log.debug("Opponent won");
      return;
    }
    log.debug(
        "Received message: {}, move: {}", message.getMessageProperties().getCorrelationId(), move);
    var nextMove = move.newMove();
    var correlationId = UUID.randomUUID().toString();
    if (nextMove.didIWin()) {
      rabbitTemplate.convertAndSend(
          message.getMessageProperties().getReplyTo(),
          move.checkmate(),
          msg -> {
            msg.getMessageProperties().setCorrelationId(correlationId);
            return msg;
          });
      log.debug("I won");
      return;
    }
    log.debug("Sending message: {}, move: {}", correlationId, nextMove);
    rabbitTemplate.convertAndSend(
        message.getMessageProperties().getReplyTo(),
        nextMove,
        msg -> {
          msg.getMessageProperties().setCorrelationId(correlationId);
          return msg;
        });
  }
}
