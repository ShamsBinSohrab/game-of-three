package app.player.domains;

import static org.apache.commons.lang3.RandomUtils.nextInt;

import java.io.Serializable;

public record Move(int number) implements Serializable {

  private static final short[] pivots = new short[] {1, 0, -1};

  public Move newMove() {
    return new Move(calculateNext());
  }

  private int calculateNext() {
    return (number + pivots[nextInt(0, 3)]) / 3;
  }
}
