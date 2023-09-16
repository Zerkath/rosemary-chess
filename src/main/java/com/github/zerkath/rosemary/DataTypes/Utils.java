package com.github.zerkath.rosemary.DataTypes;

import java.util.List;

public class Utils {

  public static short coordinateMask = 0b111;
  public static short moveMask = 0b111111;

  public static boolean isOutOfBounds(Coordinate coord) {
    return isOutOfBounds(coord.getRow(), coord.getColumn());
  }

  public static boolean isOutOfBounds(short coord) {
    return coord > 0 && coord < 64;
  }

  public static boolean isOutOfBounds(int row, int column) {
    if (row < 0 || row > 7)
      return true;
    if (column < 0 || column > 7)
      return true;

    return false;
  }

  public static short getCoordinate(int row, int column) {
    return (short) ((row << 3) + column);
  }

  public static void addToCollection(int row, int column, short origin, List<Move> moves) {
    if (isOutOfBounds(row, column))
      return;
    moves.add(new Move(origin, getCoordinate(row, column)));
  }

  public static short getCoordinate(String coordinateString) {
    return getCoordinate(
        toRowInt(coordinateString.charAt(1)),
        toColumnInt(coordinateString.charAt(0)));
  }

  private static int toRowInt(char c) {
    return 8 - Integer.parseInt(String.valueOf(c));
  }

  private static int toColumnInt(char c) {
    return c - 'a';
  }
}
