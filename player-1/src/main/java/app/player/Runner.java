package app.player;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import app.player.domains.GameInitRequest;
import app.player.events.HandshakeEvent;
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
    var request = new GameInitRequest(randomAlphanumeric(10), randomAlphanumeric(10));
    var handshake = new HandshakeEvent(request);
    applicationEventPublisher.publishEvent(handshake);
  }
}
