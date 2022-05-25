package app.player.listeners;

import app.player.events.CancelConsumerEvent;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CancelConsumerEventListener {

  private final Channel channel;

  @Async
  @EventListener
  public void onDeleteQueue(CancelConsumerEvent event) throws IOException {
    var tag = (String) event.getSource();
    channel.basicCancel(tag);
  }
}
