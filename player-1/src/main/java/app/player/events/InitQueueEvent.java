package app.player.events;

import org.springframework.context.ApplicationEvent;

public class InitQueueEvent extends ApplicationEvent {

  public InitQueueEvent(String queue) {
    super(queue);
  }
}
