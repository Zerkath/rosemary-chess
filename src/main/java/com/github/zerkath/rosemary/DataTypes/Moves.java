package com.github.zerkath.rosemary.DataTypes;

import java.util.LinkedList;

// @Deprecated // Should create a array backed impl instead allowing data to exist on stack instead
// of heap
public class Moves extends LinkedList<Short> {

  public Moves() {
    super();
  }

  /**
   * Helper constructor for testing different positions Can be used to easily add a list of moves
   * for comparisons
   *
   * @param moves moves seperated by " ", example "g1g2 f3g5"
   */
  public Moves(String moves) {
    super();
    this.addAll(moves);
  }

  public String getString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (short move : this)
      stringBuilder.append(MoveUtil.moveToString(move)).append(": 1").append('\n');
    return stringBuilder.toString();
  }

  public void add(short origin, short destination) {
    this.add(MoveUtil.getMove(origin, destination));
  }

  /**
   * Helper function for testing different positions Used to add a "g1g2 f3g5"
   *
   * @param moves moves seperated by " "
   */
  public void addAll(String moves) {
    String[] mvs = moves.split(" ");

    for (String move : mvs) {
      this.add(MoveUtil.getMove(move));
    }
  }
}
