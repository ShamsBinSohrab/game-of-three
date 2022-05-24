package app.player.events;

import java.util.UUID;
import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  public GameStartEvent(UUID gameId) {
    super(gameId);
  }
}
