package app.player.events;

import app.player.domains.Move;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventFactory {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void logSentMove(Move move, UUID correlationId) {
    applicationEventPublisher.publishEvent(new LogSentMoveEvent(move, correlationId));
  }

  public void logReceivedMove(Move move, UUID correlationId) {
    applicationEventPublisher.publishEvent(new LogReceivedMoveEvent(move, correlationId));
  }
}
