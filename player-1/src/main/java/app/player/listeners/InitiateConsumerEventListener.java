package app.player.listeners;

import static org.springframework.util.SerializationUtils.deserialize;

import app.player.domains.Move;
import app.player.events.EventFactory;
import app.player.events.InitiateConsumerEvent;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitiateConsumerEventListener {

  private final Channel channel;
  private final EventFactory eventFactory;

  @Async
  @EventListener
  public void doInitQueues(InitiateConsumerEvent event) throws IOException {
    var incomingQueue = (String) event.getSource();
    channel.queueDeclare(incomingQueue, false, true, true, Collections.emptyMap());
    channel.basicConsume(incomingQueue, deliverCallback(incomingQueue), cancelCallback());
  }

  private DeliverCallback deliverCallback(String queue) {
    return (tag, message) -> {
      var move = (Move) deserialize(message.getBody());
      if (Objects.nonNull(move)) {
        if (move.didOpponentWin()) {
          log.info("Opponent won game: {}", move.gameId());
          eventFactory.cancelConsumer(tag);
          return;
        }
        var correlationId = UUID.fromString(message.getProperties().getCorrelationId());
        eventFactory.logReceivedMove(move, correlationId);
        eventFactory.replyMove(move, tag, queue);
      }
    };
  }

  private CancelCallback cancelCallback() {
    return (tag) -> log.debug("Cancelled: " + tag);
  }
}
