package app.player;

import app.player.game.GameInitRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Listener {

  @RabbitListener(queues = "main")
  void listening(GameInitRequest request) {
    System.out.println(request);
  }
}
