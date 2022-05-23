package app.player.listeners;

import app.player.events.GameStartEvent;
import app.player.game.GameInitRequest;
import com.google.common.primitives.Longs;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameStartEventListener {

  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void doStartGame(GameStartEvent event) {
    var request = (GameInitRequest) event.getSource();
    var number = RandomUtils.nextLong(1, 100);
    var correlationId = UUID.randomUUID().toString();
    log.debug("Message {}, sending {} to {}", correlationId, number, request.outgoingQueue());
    rabbitTemplate.convertAndSend(
        request.outgoingQueue(),
        Longs.toByteArray(number),
        msg -> {
          msg.getMessageProperties().setCorrelationId(correlationId);
          msg.getMessageProperties().setReplyTo(request.incomingQueue());
          return msg;
        });
  }
}
