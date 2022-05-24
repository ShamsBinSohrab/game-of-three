package app.player.events;

import app.player.domains.Move;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ReplyMoveEvent extends ApplicationEvent {

  @Getter private final String incomingQueue;

  public ReplyMoveEvent(Move move, String incomingQueue) {
    super(move);
    this.incomingQueue = incomingQueue;
  }
}
