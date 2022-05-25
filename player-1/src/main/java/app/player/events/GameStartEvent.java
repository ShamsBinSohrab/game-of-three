package app.player.events;

import java.util.UUID;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class GameStartEvent extends ApplicationEvent {

  @Getter private final boolean userInput;

  public GameStartEvent(UUID gameId, boolean userInput) {
    super(gameId);
    this.userInput = userInput;
  }
}
