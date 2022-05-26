package app.player.listeners;

import app.player.domains.Move;
import app.player.events.LogReceivedMoveEvent;
import app.player.events.LogSentMoveEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogMoveEventListener {

  @Async
  @EventListener
  public void doLogSentMove(LogSentMoveEvent event) {
    var move = (Move) event.getSource();
    log.debug(
        "Game: {} => sent: {}, correlation: {}",
        move.gameId(),
        move.number(),
        event.getCorrelationId());
  }

  @Async
  @EventListener
  public void doLogReceivedMove(LogReceivedMoveEvent event) {
    var move = (Move) event.getSource();
    log.debug(
        "Game: {} => received: {}, correlation: {}",
        move.gameId(),
        move.number(),
        event.getCorrelationId());
  }
}
