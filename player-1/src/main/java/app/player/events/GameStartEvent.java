package app.player.events;

import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  public GameStartEvent(String queue) {
    super(queue);
  }
}
