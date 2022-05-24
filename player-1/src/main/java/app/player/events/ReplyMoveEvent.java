package app.player.events;

import app.player.domains.Move;
import java.util.UUID;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ReplyMoveEvent extends ApplicationEvent {

  @Getter private final String incomingQueue;
  @Getter private final UUID correlationId;

  public ReplyMoveEvent(Move move, String incomingQueue, UUID correlationId) {
    super(move);
    this.incomingQueue = incomingQueue;
    this.correlationId = correlationId;
  }
}