package com.github.zerkath.rosemary.DataTypes;

public class Pieces {
  public static final int EMPTY = 0b00000;
  public static final int PAWN = 0b00001;
  public static final int KNIGHT = 0b00010;
  public static final int BISHOP = 0b00011;
  public static final int ROOK = 0b00100;
  public static final int QUEEN = 0b00101;
  public static final int KING = 0b00110;
  public static final int WHITE = 0b10000;
  public static final int BLACK = 0b01000;

  public static final int m_type = 0b00111;
  public static final int m_white = 0b10000;
  public static final int m_black = 0b01000;
  public static final int m_colour = m_white | m_black;

  public static char getChar(int piece) {

    char result =
        switch (getType(piece)) {
          case PAWN -> 'p';
          case KNIGHT -> 'n';
          case BISHOP -> 'b';
          case ROOK -> 'r';
          case QUEEN -> 'q';
          case KING -> 'k';
          default -> ' ';
        };
    result = isWhite(piece) ? Character.toUpperCase(result) : result;
    return result;
  }

  public static int getNum(char piece) {
    int result = Character.isUpperCase(piece) ? WHITE : BLACK;
    piece = Character.toLowerCase(piece);
    return switch (piece) {
      case 'p' -> result | PAWN;
      case 'n' -> result | KNIGHT;
      case 'b' -> result | BISHOP;
      case 'r' -> result | ROOK;
      case 'q' -> result | QUEEN;
      case 'k' -> result | KING;
      default -> 0;
    };
  }

  public static int getType(int piece) {
    return piece & m_type;
  }

  public static int getColour(int piece) {
    return piece & m_colour;
  }

  public static boolean isWhite(int piece) {
    return getColour(piece) == WHITE;
  }
}
