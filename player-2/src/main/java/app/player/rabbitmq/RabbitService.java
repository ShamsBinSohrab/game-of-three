package app.player.rabbitmq;

public interface RabbitService {

  void addQueue(String queue, String exchange);
}
