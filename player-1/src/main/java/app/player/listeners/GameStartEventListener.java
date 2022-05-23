package app.player.listeners;

import app.player.domains.GameInitRequest;
import app.player.domains.Move;
import app.player.events.GameStartEvent;
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
    var number = RandomUtils.nextInt(1, 100);
    var move = new Move(number);
    var correlationId = UUID.randomUUID().toString();
    log.debug("Sending {}, id: {}", move.number(), correlationId);
    rabbitTemplate.convertAndSend(
        request.outgoingQueue(),
        move,
        msg -> {
          msg.getMessageProperties().setCorrelationId(correlationId);
          msg.getMessageProperties().setReplyTo(request.incomingQueue());
          return msg;
        });
  }
}
