package app.player;

import app.player.game.GameInitRequest;
import app.player.rabbitmq.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Runner implements ApplicationRunner {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitService rabbitService;
  private final Channel channel;
  private final ObjectMapper objectMapper;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    var request = new GameInitRequest("incoming", "outgoing");
    rabbitTemplate.convertAndSend("main", request, message -> message);

    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    channel.basicConsume(request.incomingQueue(), true, callback(), cancel());
  }

  DeliverCallback callback() {
    return (consumerTag, message) ->
        System.out.println("Received: " + objectMapper.readValue(message.getBody(), Integer.class));
  }

  CancelCallback cancel() {
    return consumerTag -> log.warn("Cancelled: {}", consumerTag);
  }

  @Scheduled(cron = "*/1 * * * * *")
  void run() throws JsonProcessingException {
    rabbitTemplate.convertAndSend(
        "outgoing", objectMapper.writeValueAsBytes(RandomUtils.nextInt(1, 100)));
  }
}
