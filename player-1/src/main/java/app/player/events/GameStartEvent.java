package app.player.events;

import app.player.domains.GameInitRequest;
import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  public GameStartEvent(GameInitRequest source) {
    super(source);
  }
}
