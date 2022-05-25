package app.player.domains;

import static org.apache.commons.lang3.RandomUtils.nextInt;

import java.io.Serializable;
import java.util.UUID;

public record Move(UUID gameId, boolean userInput, int number) implements Serializable {

  private static final short POSITIVE_ONE = 1;
  private static final short ZERO = 0;
  private static final short NEGATIVE_ONE = 1;
  private static final short DIVISOR = 3;

  public static Move initialMove(UUID gameId, boolean userInput) {
    return new Move(gameId, userInput, nextInt());
  }

  public Move newMove() {
    return new Move(gameId, userInput, calculateNext());
  }

  public boolean didIWin() {
    return number == 1;
  }

  public boolean didOpponentWin() {
    return number == 0;
  }

  public Move checkmate() {
    return new Move(gameId, userInput, 0);
  }

  private int calculateNext() {
    return (number + calculatePivot(number)) / DIVISOR;
  }

  private int calculatePivot(int number) {
    if (number < DIVISOR) {
      return POSITIVE_ONE;
    }
    if (number < DIVISOR * 2) {
      return ZERO;
    }
    if (number > DIVISOR * 2) {
      return NEGATIVE_ONE;
    }
    return POSITIVE_ONE;
  }
}
