package app.player;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import app.player.game.GameInitRequest;
import app.player.rabbitmq.RabbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitService rabbitService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var incomingQueue = randomAlphanumeric(10);
    var outgoingQueue = randomAlphanumeric(10);
    var request = new GameInitRequest(incomingQueue, outgoingQueue);
    rabbitService.addQueue(outgoingQueue, outgoingQueue);
    rabbitTemplate.convertAndSend("main", request);
  }
}
