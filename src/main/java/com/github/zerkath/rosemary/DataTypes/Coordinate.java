package com.github.zerkath.rosemary.DataTypes;

/**
 * TODO: Should be converted to a int value with methods for converting between
 * string to coord and vice versa
 * Accounts for 17.3% of total allocated objects at perft 4
 */
public class Coordinate {

  public short coord;


  private void fromRowAndCol(int row, int col) {
    this.coord = Utils.getCoordinate(row, col);
  }

  public Coordinate(short coord) {
    this.coord = coord;
  }

  public Coordinate(int row, int column) {
    fromRowAndCol(row, column);
  }

  public Coordinate(String coordinateString) {
    int column = Utils.toColumnInt(coordinateString.charAt(0));
    int row = Utils.toRowInt(coordinateString.charAt(1));
    fromRowAndCol(row, column);
  }

  public short getRow() {
    return (short)(coord >> 3);
  }

  public short getColumn() {
    return (short)(coord & Utils.coordinateMask);
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
