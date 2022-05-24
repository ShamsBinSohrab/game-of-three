package app.player.events;

import org.springframework.context.ApplicationEvent;

public class InitiateConsumerEvent extends ApplicationEvent {

  public InitiateConsumerEvent(String queue) {
    super(queue);
  }
}
