package app.player.listeners;

import app.player.domains.Move;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "game-queue")
  void listenGameRequest(Move move, Message message) {
    var nextMove = move.newMove();
    var correlationData = new CorrelationData();
    rabbitTemplate.convertAndSend(
        message.getMessageProperties().getReplyTo(), nextMove, correlationData);
  }
}
