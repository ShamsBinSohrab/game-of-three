package app.player.web;

import app.player.events.GameStartEvent;
import app.player.web.models.GameRequest;
import app.player.web.models.GameResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

  private final ApplicationEventPublisher applicationEventPublisher;

  @PostMapping("/start")
  @ResponseStatus(HttpStatus.ACCEPTED)
  GameResponse start(@RequestBody GameRequest request) {
    var gameId = UUID.randomUUID();
    applicationEventPublisher.publishEvent(new GameStartEvent(gameId, request.userInput()));
    return new GameResponse(gameId);
  }
}
