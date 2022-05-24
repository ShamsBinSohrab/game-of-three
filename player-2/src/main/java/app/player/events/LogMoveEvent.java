package app.player.events;

import app.player.domains.Move;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public abstract class LogMoveEvent extends ApplicationEvent {

  @Getter private final UUID correlationId;

  public LogMoveEvent(Move move, UUID correlationId) {
    super(move);
    this.correlationId = correlationId;
  }
}
