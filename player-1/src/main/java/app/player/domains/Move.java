package app.player.domains;

import java.io.Serializable;

public record Move(int number) implements Serializable {

  private static final short POSITIVE_ONE = 1;
  private static final short ZERO = 0;
  private static final short NEGATIVE_ONE = 1;
  private static final short DIVISOR = 3;

  public Move newMove() {
    return new Move(calculateNext());
  }

  public boolean didIWin() {
    return number == 1;
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
