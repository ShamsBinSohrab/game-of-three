package app.player;

import app.player.game.GameInitRequest;
import app.player.rabbitmq.RabbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

  private final RabbitService rabbitService;
  private final Channel channel;
  private final ObjectMapper objectMapper;
  private final RabbitTemplate rabbitTemplate;

  @RabbitListener(queues = "main")
  void listenGameRequest(GameInitRequest request) throws IOException, InterruptedException {
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    channel.basicConsume(request.outgoingQueue(), true, callback(), cancel());
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
        "incoming", objectMapper.writeValueAsBytes(RandomUtils.nextInt(1, 100)));
  }
}
