package com.github.zerkath.rosemary.DataTypes;

import java.util.ArrayList;
import java.util.List;

public class Moves {

  private int maxSize = 128;
  private int writePointer = -1;
  private int readPointer = -1;
  private short moves[] = new short[maxSize];

  public Moves() {}

  public List<Short> asList() {
    List<Short> list = new ArrayList<>();
    while (this.hasNext()) {
      list.add(this.next());
    }
    this.resetReadHead();
    return list;
  }

  public int size() {
    return writePointer + 1;
  }

  public void resetReadHead() {
    readPointer = -1;
  }

  public boolean hasNext() {
    return readPointer < writePointer;
  }

  public short first() {
    return moves[0];
  }

  public short next() {
    readPointer++;
    return moves[readPointer];
  }

  public void add(short move) {
    writePointer += 1;
    moves[writePointer] = move;
  }

  /**
   * Helper constructor for testing different positions Can be used to easily add a list of moves
   * for comparisons
   *
   * @param moves moves seperated by " ", example "g1g2 f3g5"
   */
  public Moves(String moves) {
    this.addAll(moves);
  }

  public String getString() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < writePointer; i++)
      stringBuilder.append(MoveUtil.moveToString(moves[i])).append(": 1").append('\n');
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
