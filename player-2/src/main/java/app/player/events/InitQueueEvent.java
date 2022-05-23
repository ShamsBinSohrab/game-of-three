package app.player.events;

import app.player.domains.GameInitRequest;
import org.springframework.context.ApplicationEvent;

public class InitQueueEvent extends ApplicationEvent {

  public InitQueueEvent(GameInitRequest source) {
    super(source);
  }
}
