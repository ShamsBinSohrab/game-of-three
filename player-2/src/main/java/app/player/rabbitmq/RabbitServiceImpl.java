package app.player.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RabbitServiceImpl implements RabbitService {

  private final RabbitAdmin rabbitAdmin;
  private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

  @Override
  public void addQueue(String queueName, String exchangeName) {
    var queue = new Queue(queueName, false, false, true);
    var exchange = new DirectExchange(exchangeName);
    var binding = BindingBuilder.bind(queue).to(exchange).with("");
    rabbitAdmin.declareExchange(exchange);
    rabbitAdmin.declareQueue(queue);
    rabbitAdmin.declareBinding(binding);
  }
}
