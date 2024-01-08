package com.github.zerkath.rosemary.DataTypes;

import java.util.Arrays;

public class Board {

  private final byte[] board = new byte[64];
  private CastlingRights whiteCastlingRights = CastlingRights.NONE;
  private CastlingRights blackCastlingRights = CastlingRights.NONE;
  private final StringBuilder strBuilder = new StringBuilder();
  private short whiteKing = -1;
  private short blackKing = -1;

  public Board() {
    for (int i = 0; i < 64; i++) this.board[i] = 0;
  }

  public Board(Board board) {
    System.arraycopy(board.board, 0, this.board, 0, 64);
    this.whiteCastlingRights = board.whiteCastlingRights;
    this.blackCastlingRights = board.blackCastlingRights;
    this.whiteKing = board.whiteKing;
    this.blackKing = board.blackKing;
  }

  public byte[] getBoard() {
    return this.board;
  }

  public byte getCoordinate(short coordinate) {
    return this.board[coordinate]; // TODO validation?
  }

  public byte getCoordinate(int row, int column) {
    return this.board[(row << 3) | column];
  }

  public void clearCoordinate(short coord) {
    this.board[coord] = 0;
  }

  public void clearCoordinate(int row, int column) {
    this.board[(row << 3) | column] = 0;
  }

  public byte[] getRow(int row) {
    int startIndex = row << 3;
    int endIndex = startIndex + 8;
    return Arrays.copyOfRange(board, startIndex, endIndex);
  }

  public void replaceCoordinate(int row, int column, byte piece) {
    this.board[(row << 3) | column] = piece;
  }

  public void replaceCoordinate(short coordinate, byte piece) {
    if (piece != 0 && Pieces.getType(piece) == Pieces.KING) replaceKing(coordinate, piece);
    replaceCoordinate(MoveUtil.getRow(coordinate), MoveUtil.getColumn(coordinate), piece);
  }

  private void replaceKing(short coordinate, int piece) {
    if (Pieces.isWhite(piece)) setWhiteKing(coordinate);
    else setBlackKing(coordinate);
  }

  public CastlingRights getWhiteCastling() {
    return whiteCastlingRights;
  }

  public CastlingRights getBlackCastling() {
    return blackCastlingRights;
  }

  public void setWhiteCastlingRights(CastlingRights rights) {
    whiteCastlingRights = rights;
  }

  public void setBlackCastlingRights(CastlingRights rights) {
    blackCastlingRights = rights;
  }

  public void setCastling(CastlingRights rights, boolean white) {
    if (white) setWhiteCastlingRights(rights);
    else setBlackCastlingRights(rights);
  }

  public short getWhiteKing() {
    return whiteKing;
  }

  public short getBlackKing() {
    return blackKing;
  }

  public void setBlackKing(short coordinate) {
    this.blackKing = coordinate;
  }

  public void setWhiteKing(short coordinate) {
    this.whiteKing = coordinate;
  }

  public boolean isOpposingColourOrEmpty(short destination, int originalPiece) {
    if (Utils.isOutOfBounds(destination)) return false;

    boolean isWhite = Pieces.isWhite(originalPiece);
    int dest = getCoordinate(destination);
    if (dest == Pieces.EMPTY) return true;
    boolean opponent = Pieces.isWhite(dest);

    return isWhite != opponent;
  }

  /** Valid move in this context means either empty or opposing colour */
  public boolean isValidMove(short move) {
    return Pieces.EMPTY == getCoordinate(MoveUtil.getDestination(move))
        || Pieces.isWhite(getCoordinate(MoveUtil.getOrigin(move)))
            != Pieces.isWhite(getCoordinate(MoveUtil.getDestination(move)));
  }

  public boolean pawnCapturePossible(short coordinate, int origin) {
    return getCoordinate(coordinate) != Pieces.EMPTY && isOpposingColourOrEmpty(coordinate, origin);
  }

  public String toString() {
    strBuilder.setLength(0);
    String divider = "=|-----|-----|-----|-----|-----|-----|-----|-----|=\n";
    strBuilder.append("    0     1     2     3     4     5     6     7\n");
    for (int row = 0; row < 8; row++) {
      strBuilder.append(divider).append(row);
      for (int column = 0; column < 8; column++) {
        int piece = this.getCoordinate(row, column);
        if (piece != 0) {
          strBuilder.append("|  ").append(Pieces.getChar(piece));
        } else {
          strBuilder.append("|   ");
        }
        if (column != 7) strBuilder.append("  ");
        else strBuilder.append("  |");
      }
      strBuilder.append(8 - row);
      strBuilder.append("\n");
    }
    strBuilder.append(divider);
    strBuilder.append("    a     b     c     d     e     f     g     h\n");

    return strBuilder.toString();
  }
}
