package app.player.listeners;

import app.player.domains.Move;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class CalculateMoveEventListener {

  @Async
  @EventListener
  public void doCalculateMove(Move move) {}
}
