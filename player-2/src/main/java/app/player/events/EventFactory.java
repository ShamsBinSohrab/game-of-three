package app.player.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventFactory {

  private final ApplicationEventPublisher applicationEventPublisher;
}
