package com.github.zerkath.rosemary.DataTypes;

/**
 * TODO: Should be converted to a int value with methods for converting between
 * string to coord and vice versa
 * Accounts for 17.3% of total allocated objects at perft 4
 */
public class Coordinate {

  public short coord;


  private void fromRowAndCol(int row, int col) {
    this.coord = (short) ((row << 3) | col);
  }

  public Coordinate(short coord) {
    this.coord = coord;
  }

  public Coordinate(int row, int column) {
    fromRowAndCol(row, column);
  }

  public Coordinate(String coordinateString) {
    int column = toColumnInt(coordinateString.charAt(0));
    int row = toRowInt(coordinateString.charAt(1));
    fromRowAndCol(row, column);
  }

  public short getRow() {
    return (short)(coord >> 3);
  }

  public short getColumn() {
    return (short)(coord & Utils.coordinateMask);
  }

  public String toString() {
    int row = (coord >> 3);
    int column = (coord & Utils.coordinateMask);

    return toColumnChar(column) + "" + toRowChar(row);
  }

  private char toColumnChar(int i) {
    return (char) (i + 'a');
  }

  private int toColumnInt(char c) {
    return c - 'a';
  }

  private char toRowChar(int i) {
    return Character.forDigit(8 - i, 10);
  }

  private int toRowInt(char c) {
    return 8 - Integer.parseInt(String.valueOf(c));
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coordinate))
      return false;
    Coordinate c = (Coordinate) obj; // cast for types
    return this.coord == c.coord;
  }

  @Override
  public int hashCode() {
    return coord;
  }
}
