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
    var move1 = new Move(nextInt(1, 100));
    var gameStartEvent1 = new GameStartEvent(move1);
    applicationEventPublisher.publishEvent(gameStartEvent1);

    var move2 = new Move(nextInt(1, 100));
    var gameStartEvent2 = new GameStartEvent(move2);
    applicationEventPublisher.publishEvent(gameStartEvent2);
  }
}
