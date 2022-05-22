package app.player;

import app.player.game.GameInitRequest;
import app.player.rabbitmq.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final RabbitService rabbitService;

  @RabbitListener(queues = "main")
  void listenGameRequest(GameInitRequest request) {
    rabbitService.addQueue(request.incomingQueue(), request.incomingQueue());
  }
}
