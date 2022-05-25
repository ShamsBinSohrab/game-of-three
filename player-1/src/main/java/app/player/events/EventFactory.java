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

  public void gameStart(UUID gameId, boolean userInput) {
    applicationEventPublisher.publishEvent(new GameStartEvent(gameId, userInput));
  }

  public void initiateConsumer(String queue) {
    applicationEventPublisher.publishEvent(new InitiateConsumerEvent(queue));
  }

  public void logSentMove(Move move, UUID correlationId) {
    applicationEventPublisher.publishEvent(new LogSentMoveEvent(move, correlationId));
  }

  public void logReceivedMove(Move move, UUID correlationId) {
    applicationEventPublisher.publishEvent(new LogReceivedMoveEvent(move, correlationId));
  }

  public void cancelConsumer(String tag) {
    applicationEventPublisher.publishEvent(new CancelConsumerEvent(tag));
  }

  public void replyMove(Move move, String tag, String queue) {
    applicationEventPublisher.publishEvent(new ReplyMoveEvent(move, tag, queue));
  }
}
