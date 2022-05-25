package app.player.listeners;

import app.player.domains.Move;
import app.player.events.EventFactory;
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

  private final EventFactory eventFactory;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "game-queue", concurrency = "5")
  void listenGameRequest(Move move, Message message) {
    if (move.didOpponentWin()) {
      log.info("Opponent won game: {}", move.gameId());
      return;
    }
    var receivedCorrelationId = UUID.fromString(message.getMessageProperties().getCorrelationId());
    eventFactory.logReceivedMove(move, receivedCorrelationId);

    var nextMove = move.nextMove();
    var correlationId = UUID.randomUUID();
    if (nextMove.didIWin()) {
      log.info("I won game: {}", nextMove.gameId());
      send(message.getMessageProperties().getReplyTo(), nextMove.checkmate(), correlationId);
      return;
    }
    send(message.getMessageProperties().getReplyTo(), nextMove, correlationId);
    eventFactory.logSentMove(nextMove, correlationId);
  }

  private void send(String replyQueue, Move move, UUID correlationId) {
    rabbitTemplate.convertAndSend(
        replyQueue,
        move,
        message -> {
          message.getMessageProperties().setCorrelationId(correlationId.toString());
          return message;
        });
  }
}
