package app.player.events;

import app.player.domains.Move;
import java.util.UUID;

public class LogSentMoveEvent extends LogMoveEvent {

  public LogSentMoveEvent(Move move, UUID correlationId) {
    super(move, correlationId);
  }
}
