package app.player;

import static org.apache.commons.lang3.RandomUtils.nextInt;

import app.player.domains.Move;
import app.player.events.GameStartEvent;
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
    var move = new Move(nextInt(1, 100));
    var gameStartEvent = new GameStartEvent(move);
    applicationEventPublisher.publishEvent(gameStartEvent);
  }
}
