package app.player.events;

import app.player.game.GameInitRequest;
import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  public GameStartEvent(GameInitRequest source) {
    super(source);
  }
}
