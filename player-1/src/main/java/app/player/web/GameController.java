package app.player.web;

import app.player.events.EventFactory;
import app.player.web.models.GameRequest;
import app.player.web.models.GameResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  private final EventFactory eventFactory;

  @PostMapping("/start")
  @ResponseStatus(HttpStatus.ACCEPTED)
  GameResponse start(@RequestBody GameRequest request) {
    var gameId = UUID.randomUUID();
    eventFactory.gameStart(gameId, request.number());
    return new GameResponse(gameId);
  }
}
