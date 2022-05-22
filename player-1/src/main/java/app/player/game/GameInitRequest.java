package app.player.game;

import java.io.Serializable;

public record GameInitRequest(String incomingQueue, String outgoingQueue) implements Serializable {}
