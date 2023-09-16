package com.github.zerkath.rosemary.DataTypes;

/**
 * Used for storing basic information about moves
 * From and to where
 * also promotion
 *
 * TODO: Could convert this to a int value, and use bit shifting to store the
 * values
 * Reason for this is that at perft 4 this accounts for 5.6% of total allocated
 * objects
 */
public class Move {

  // TODO: rename this value after doing changes
  public short p_move;

  // TODO: Should cram this promotion ito the move short value
  public int promotion;

  private void fromOriginAndDestination(short origin, short destination) {
    this.p_move = (short) ((origin << 6) | destination);
  }

  public Move(short origin, short destination) {
    fromOriginAndDestination(origin, destination);
  }

  public Move(Move move, int promotion) {
    this.p_move = move.p_move;
    this.promotion = promotion;
  }

  public Move(String move) {

    short origin = Utils.getCoordinate(move.substring(0, 2));
    short destination = Utils.getCoordinate(move.substring(2, 4));

    fromOriginAndDestination(origin, destination);

    this.promotion = 0;
    if (move.length() == 5) {
      this.promotion = Pieces.getNum(move.charAt(4));
    }
  }

  public short getOrigin() {
    return (short) (p_move >> 6);
  }

  public short getDestination() {
    return (short) (p_move & Utils.coordinateMask);
  }

  public String toString() {
    String sMove = Utils.moveToString(this.p_move);
    if (promotion != 0) {
      sMove += Pieces.getChar(promotion);
    }
    return sMove;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Move))
      return false;
    Move otherMove = (Move) other;
    return this.p_move == otherMove.p_move && this.promotion == otherMove.promotion;
  }
}
