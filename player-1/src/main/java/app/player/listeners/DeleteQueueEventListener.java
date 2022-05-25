package app.player.listeners;

import app.player.events.DeleteQueueEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteQueueEventListener {

  private final RabbitAdmin rabbitAdmin;

  @Async
  @EventListener
  public void onDeleteQueue(DeleteQueueEvent event) {
    var queue = (String) event.getSource();
    if (rabbitAdmin.deleteQueue(queue)) {
      log.debug("Queue: {} has been deleted", queue);
    }
  }
}
