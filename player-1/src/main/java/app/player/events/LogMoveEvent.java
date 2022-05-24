package app.player.events;

import app.player.domains.Move;
import java.util.UUID;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public abstract class LogMoveEvent extends ApplicationEvent {

  @Getter private final UUID correlationId;

  public LogMoveEvent(Move move, UUID correlationId) {
    super(move);
    this.correlationId = correlationId;
  }
}
