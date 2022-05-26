package app.player.domains;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;

@Slf4j
public record Move(UUID gameId, int number) implements Serializable {

  private static final int POSITIVE_ONE = 1;
  private static final int ZERO = 0;
  private static final int NEGATIVE_ONE = -1;
  private static final int DIVISOR = 3;

  public static Move initialMove(UUID gameId, int number) {
    return new Move(gameId, number);
  }

  public Move nextMove() {
    return new Move(gameId, calculateNext());
  }

  public boolean didIWin() {
    return number == 1;
  }

  public boolean didOpponentWin() {
    return number == 0;
  }

  public Move checkmate() {
    return new Move(gameId, 0);
  }

  private int calculateNext() {
    int numberToAdd = getNumberToAdd(number);
    int newNumber = (number + numberToAdd) / DIVISOR;
    log.info("Initial: {}, Added: {}, Result: {}", number, numberToAdd, newNumber);
    return newNumber;
  }

  private int getNumberToAdd(int number) {
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
