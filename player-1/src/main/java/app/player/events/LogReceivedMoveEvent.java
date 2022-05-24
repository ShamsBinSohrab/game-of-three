package app.player.events;

import app.player.domains.Move;
import java.util.UUID;

public class LogReceivedMoveEvent extends LogMoveEvent {

  public LogReceivedMoveEvent(Move move, UUID correlationId) {
    super(move, correlationId);
  }
}
