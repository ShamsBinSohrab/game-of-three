package app.player.listeners;

import app.player.domains.Move;
import app.player.events.LogReceivedMoveEvent;
import app.player.events.LogSentMoveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameRequestListener {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationEventPublisher applicationEventPublisher;

    @RabbitListener(queues = "game-queue")
    void listenGameRequest(Move move, Message message) {
        var receivedCorrelationId = UUID.fromString(message.getMessageProperties().getCorrelationId());
        applicationEventPublisher.publishEvent(new LogReceivedMoveEvent(move, receivedCorrelationId));

        var nextMove = move.newMove();
        var replyToQueue = message.getMessageProperties().getReplyTo();
        if (nextMove.didIWin()) {
            log.debug("I won");
        } else {
            var correlationId = UUID.randomUUID();
            rabbitTemplate.convertAndSend(
                    replyToQueue, nextMove, messagePostProcessor(correlationId, replyToQueue));
            applicationEventPublisher.publishEvent(new LogSentMoveEvent(nextMove, correlationId));
        }
    }

    private MessagePostProcessor messagePostProcessor(UUID correlationId, String incomingQueue) {
        return message -> {
            message.getMessageProperties().setCorrelationId(correlationId.toString());
            message.getMessageProperties().setReplyTo(incomingQueue);
            return message;
        };
    }
}
