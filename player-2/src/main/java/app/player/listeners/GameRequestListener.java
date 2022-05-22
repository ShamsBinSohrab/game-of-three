package app.player.listeners;

import app.player.events.InitQueueEvent;
import app.player.game.GameInitRequest;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final Channel channel;
  private final ApplicationEventPublisher applicationEventPublisher;

  @RabbitListener(queues = "game-of-three")
  void listenGameRequest(GameInitRequest request) throws IOException {
    log.debug(
        "Received handshake request, incoming: {} and outgoing: {}",
        request.incomingQueue(),
        request.outgoingQueue());
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    applicationEventPublisher.publishEvent(new InitQueueEvent(request));
  }
}
