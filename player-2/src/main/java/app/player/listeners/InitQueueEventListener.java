package app.player.listeners;

import app.player.events.InitQueueEvent;
import app.player.game.GameInitRequest;
import com.google.common.primitives.Longs;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitQueueEventListener {

  private final Channel channel;
  private final RabbitTemplate rabbitTemplate;

  @Async
  @EventListener
  public void doInitQueues(InitQueueEvent event) throws IOException {
    var request = (GameInitRequest) event.getSource();
    channel.queueDeclare(request.incomingQueue(), false, false, true, Collections.emptyMap());
    channel.queueDeclare(request.outgoingQueue(), false, false, true, Collections.emptyMap());
    channel.basicConsume(
        request.outgoingQueue(), deliverCallback(request.outgoingQueue()), cancelCallback());
  }

  private DeliverCallback deliverCallback(String replyTo) {
    return (tag, message) -> {
      var number = Longs.fromByteArray(message.getBody());
      var routingKey = message.getProperties().getReplyTo();
      log.debug("Received : {} from {}", number, message.getEnvelope().getRoutingKey());
      sleep(1);
      rabbitTemplate.convertAndSend(
          routingKey,
          Longs.toByteArray(RandomUtils.nextLong(1, 100)),
          msg -> {
            msg.getMessageProperties().setReplyTo(replyTo);
            return msg;
          });
    };
  }

  private CancelCallback cancelCallback() {
    return (tag) -> log.debug("Cancelled: " + tag);
  }

  static void sleep(int time) {
    try {
      TimeUnit.SECONDS.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
