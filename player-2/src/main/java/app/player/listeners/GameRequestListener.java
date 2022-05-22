package app.player.listeners;

import app.player.game.GameInitRequest;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final Channel channel;

  @RabbitListener(queues = "game-of-three")
  void listenGameRequest(GameInitRequest request) throws IOException, InterruptedException {
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
  }
}
