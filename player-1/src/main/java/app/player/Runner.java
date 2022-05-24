package app.player;

import app.player.events.GameStartEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void run(ApplicationArguments args) {
    var gameId = UUID.randomUUID();
    var gameStartEvent = new GameStartEvent(gameId);
    applicationEventPublisher.publishEvent(gameStartEvent);
  }
}
