package com.github.zerkath.rosemary.DataTypes;

/**
 * TODO: Should be converted to a int value with methods for converting between string to coord and vice versa
 * Accounts for 17.3% of total allocated objects at perft 4
 */
public class Coordinate {
    public int column;
    public int row;

    public Coordinate(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public Coordinate(String coordinateString) {
        column = toColumnInt(coordinateString.charAt(0));
        row = toRowInt(coordinateString.charAt(1));
    }

    public String toString() {
        return toColumnChar(column) + "" + toRowChar(row);
    }

    private char toColumnChar(int i) {
        return (char)(i + 'a');
    }

    private int toColumnInt(char c) {
        return c - 'a';
    }

    private char toRowChar(int i) {
        return Character.forDigit(8-i, 10);
    }

    private int toRowInt(char c) {
        return 8 - Integer.parseInt(String.valueOf(c));
    }

    @Override
    public boolean equals(Object coord) {
        if (!(coord instanceof Coordinate)) return false;
        Coordinate c = (Coordinate) coord; // cast for types
        return c.column == this.column && c.row == this.row;
    }

    @Override
    public int hashCode() {
        return column * 7 + row * 17;
    }
}
