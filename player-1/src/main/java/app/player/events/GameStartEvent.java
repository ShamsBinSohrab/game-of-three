package app.player.events;

import java.util.Optional;
import java.util.UUID;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.Nullable;

public class GameStartEvent extends ApplicationEvent {

  private final Integer number;

  public GameStartEvent(UUID gameId, @Nullable Integer number) {
    super(gameId);
    this.number = number;
  }

  public Optional<Integer> getNumber() {
    return Optional.ofNullable(number);
  }
}
