package app.player.events;

import app.player.domains.Move;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ReplyMoveEvent extends ApplicationEvent {

  @Getter private final String tag;
  @Getter private final String incomingQueue;

  public ReplyMoveEvent(Move move, String tag, String incomingQueue) {
    super(move);
    this.tag = tag;
    this.incomingQueue = incomingQueue;
  }
}
