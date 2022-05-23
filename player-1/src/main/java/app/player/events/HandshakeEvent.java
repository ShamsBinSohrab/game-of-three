package app.player.events;

import app.player.domains.GameInitRequest;
import org.springframework.context.ApplicationEvent;

public class HandshakeEvent extends ApplicationEvent {

  public HandshakeEvent(GameInitRequest source) {
    super(source);
  }
}
