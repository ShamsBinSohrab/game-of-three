package app.player.events;

import app.player.game.GameInitRequest;
import org.springframework.context.ApplicationEvent;

public class HandshakeEvent extends ApplicationEvent {

  public HandshakeEvent(GameInitRequest source) {
    super(source);
  }
}
