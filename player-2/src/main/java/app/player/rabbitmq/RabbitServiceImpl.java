package app.player.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RabbitServiceImpl implements RabbitService {

  @Override
  public void addQueue(String queueName, String exchangeName) {

  }
}
