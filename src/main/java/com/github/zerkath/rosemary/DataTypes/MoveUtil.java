package com.github.zerkath.rosemary.DataTypes;

/**
 * Move representation short. 12bits: 6 bits for origin, 6 bits for destination 0b000_000_000_000 =
 * 0, 0 0b000_000_111_111 = 0, 63 0b111_111_000_000 = 63, 0 0b111_111_111_111 = 63, 63
 *
 * <p>4 bits header, for promotions
 *
 * <p>1 bits for color 0b1000 = is white 0b0000 = is black
 *
 * <p>3 bits for promotion 0b111 = promotion to queen 0b110 = promotion to rook 0b101 = promotion to
 * bishop 0b100 = promotion to knight
 *
 * <p>to check if there is a promotion can assert if x != 0
 */
public class MoveUtil {

  public static short coordinateMask = 0b111;
  public static short moveMask = 0b111111;

  private static short fromOriginAndDestination(short origin, short destination) {
    return (short) ((origin << 6) | destination);
  }

  public static short getMove(short origin, short destination) {
    return fromOriginAndDestination(origin, destination);
  }

  public static short getMove(String move) {
    short origin = Utils.getCoordinate(move.substring(0, 2));
    short destination = Utils.getCoordinate(move.substring(2, 4));

    int piece = 0;
    if (move.length() == 5) {
      Pieces.getNum(move.charAt(4));
    }
    return getMove(getMove(origin, destination), Pieces.getType(piece), Pieces.isWhite(piece));
  }

  public static short getPromotion(short move) {
    return (short) (move >> 12);
  }

  public static short getMove(short move, int piece, boolean isWhite) {
    short cleanMove = clearPromotion(move);
    short promotion = 0;
    switch (piece) {
      case Pieces.QUEEN -> promotion = 0b111;
      case Pieces.ROOK -> promotion = 0b110;
      case Pieces.BISHOP -> promotion = 0b101;
      case Pieces.KNIGHT -> promotion = 0b100;
    }
    if (isWhite) promotion |= 0b1000;
    return (short) (cleanMove | (promotion << 12));
  }

  public static short clearPromotion(short move) {
    return (short) (move & moveMask);
  }

  public static short getOrigin(short move) {
    return (short) ((move >> 6) & coordinateMask);
  }

  public static short getDestination(short move) {
    return (short) (move & coordinateMask);
  }

  public static String moveToString(short move) {
    int origin = (move >> 6);
    int destination = (move & Utils.moveMask);

    return coordinateToString((short) origin) + "" + coordinateToString((short) destination);
  }

  public static String coordinateToString(short coordinate) {
    short masked = (short) (coordinate & moveMask);
    int row = masked >> 3;
    int column = masked & coordinateMask;

    return Utils.toColumnChar(column) + "" + Utils.toRowChar(row);
  }

  public static short getRow(short coord) {
    return (short) ((coord >> 3) & coordinateMask);
  }

  public static short getColumn(short coord) {
    return (short) (coord & coordinateMask);
  }
}
