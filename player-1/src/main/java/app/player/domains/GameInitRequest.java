package app.player.domains;

import java.io.Serializable;

public record GameInitRequest(String incomingQueue, String outgoingQueue) implements Serializable {}
