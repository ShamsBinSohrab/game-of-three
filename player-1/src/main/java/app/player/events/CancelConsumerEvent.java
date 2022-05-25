package app.player.events;

import org.springframework.context.ApplicationEvent;

public class CancelConsumerEvent extends ApplicationEvent {

  public CancelConsumerEvent(String tag) {
    super(tag);
  }
}
