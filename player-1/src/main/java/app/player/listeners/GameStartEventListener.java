package app.player.listeners;

import app.player.events.GameStartEvent;
import app.player.game.GameInitRequest;
import com.google.common.primitives.Longs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameStartEventListener {

  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void doStartGame(GameStartEvent event) {
    var request = (GameInitRequest) event.getSource();
    rabbitTemplate.convertAndSend(
        request.outgoingQueue(), Longs.toByteArray(RandomUtils.nextLong(1, 100)));
  }
}
