package app.player.configurations;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

  @Bean
  public Queue queue(@Value("${game.queue.name}") String queueName) {
    return QueueBuilder.nonDurable(queueName).build();
  }

  @Bean
  public DirectExchange exchange(@Value("${game.exchange.name}") String exchangeName) {
    return ExchangeBuilder.directExchange(exchangeName).build();
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).withQueueName();
  }

  @Bean
  public Channel channel(ConnectionFactory connectionFactory) {
    return connectionFactory.createConnection().createChannel(true);
  }
}
