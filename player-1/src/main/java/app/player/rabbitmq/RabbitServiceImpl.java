package app.player.rabbitmq;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RabbitServiceImpl implements RabbitService {

  private final Channel channel;

  @Override
  public void addQueue(String queueName, String exchangeName) {
    try {
      channel.queueDeclare(queueName, false, false, true, Collections.emptyMap());
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
    }
  }
}
