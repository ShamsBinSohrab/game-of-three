package app.player.events;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import app.player.domains.Move;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  @Getter private final String replyToQueue = randomAlphanumeric(10);

  public GameStartEvent(Move move) {
    super(move);
  }
}
