package app.player.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RabbitServiceImpl implements RabbitService {

  private final RabbitAdmin rabbitAdmin;

  @Override
  public void addQueue() {}
}
