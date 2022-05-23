package app.player.events;

import app.player.domains.Move;
import org.springframework.context.ApplicationEvent;

public class CalculateMoveEvent extends ApplicationEvent {

  public CalculateMoveEvent(Move move) {
    super(move);
  }
}
