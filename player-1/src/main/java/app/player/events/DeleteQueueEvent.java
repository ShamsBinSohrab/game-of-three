package app.player.events;

import org.springframework.context.ApplicationEvent;

public class DeleteQueueEvent extends ApplicationEvent {

  public DeleteQueueEvent(String queue) {
    super(queue);
  }
}
