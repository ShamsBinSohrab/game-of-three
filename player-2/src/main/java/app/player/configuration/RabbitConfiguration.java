package app.player.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

  @Bean
  public Queue queue(@Value("${game.queue.name}") String queueName) {
    return new Queue(queueName, true);
  }

  @Bean
  public DirectExchange exchange(@Value("${game.exchange.name}") String exchange) {
    return new DirectExchange(exchange);
  }

  @Bean
  public Binding binding(Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with("");
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      CachingConnectionFactory connectionFactory, RabbitTemplateConfigurer configurer) {
    var template = new RabbitTemplate();
    configurer.configure(template, connectionFactory);
    return template;
  }

  @Bean
  public CachingConnectionFactory getConnectionFactory(RabbitProperties properties) {
    var factory = new CachingConnectionFactory();
    factory.setHost(properties.getHost());
    factory.setPort(properties.getPort());
    factory.setUsername(properties.getUsername());
    factory.setPassword(properties.getPassword());
    factory.setVirtualHost(properties.getVirtualHost());
    return factory;
  }
}
