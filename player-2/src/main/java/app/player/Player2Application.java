package app.player;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class Player2Application {

  public static void main(String[] args) {
    SpringApplication.run(Player2Application.class, args);
  }
}
