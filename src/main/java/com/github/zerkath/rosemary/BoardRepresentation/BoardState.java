package com.github.zerkath.rosemary.BoardRepresentation;

import com.github.zerkath.rosemary.DataTypes.*;
import java.util.HashMap;
import java.util.Map;

public class BoardState {

  public Board board = new Board();
  public boolean isWhiteTurn;

  public int turnNumber = 1;
  public int halfMove = 0;

  public Map<Byte, Integer> pieceMap = new HashMap<>();

  public short enPassant = -1;

  public BoardState() {}

  public BoardState(BoardState state) {
    setBoardState(state);
  }

  public BoardState(String fen) {
    setBoardState(FenUtils.parseFen(fen));
  }

  public void setBoardState(BoardState state) {
    this.board = new Board(state.board);
    this.isWhiteTurn = state.isWhiteTurn;
    this.turnNumber = state.turnNumber;
    this.halfMove = state.halfMove;
    this.enPassant = state.enPassant;
  }

  public void setCastling(char[] castling) {
    if (castling.length == 1 && castling[0] == '-') {
      board.setWhiteCastlingRights(CastlingRights.NONE);
      board.setBlackCastlingRights(CastlingRights.NONE);
      return;
    }

    for (char c : castling) {
      boolean white = !Character.isLowerCase(c);
      boolean queen = Character.toLowerCase(c) == 'q';
      CastlingRights curr = white ? board.getWhiteCastling() : board.getBlackCastling();
      if (curr == CastlingRights.NONE) {
        curr = queen ? CastlingRights.QUEEN : CastlingRights.KING;
      } else if (curr == CastlingRights.BOTH) {
        break;
      } else {
        if (curr == CastlingRights.QUEEN) {
          if (!queen) curr = CastlingRights.BOTH;
        } else {
          if (queen) curr = CastlingRights.BOTH;
        }
      }

      board.setCastling(curr, white);
    }
  }

  /**
   * used to add rows of FEN data to the board state
   *
   * @param rowData a row of FEN
   * @param row which row to place the fen
   */
  public void addRow(String rowData, int row) {
    int column = 0;
    for (Character ch : rowData.toCharArray()) {
      if (Character.isDigit(ch)) {
        int numOfEmpty = Character.digit(ch, 10);
        for (int j = 0; j < numOfEmpty; j++) {
          board.clearCoordinate(row, column);
          column++;
        }
      } else {
        byte piece = Pieces.getNum(ch);
        board.replaceCoordinate(Utils.getCoordinate(row, column), piece);
        incrementPiece(piece);
        column++;
      }
    }
  }

  public BoardState playMoves(String[] moves) {
    BoardState bs = this;
    for (String move : moves) {
      short x = MoveUtil.getMove(move);
      System.out.println(MoveUtil.moveToString(x));
      bs = bs.makeMove(x);
    }
    return bs;
  }

  public BoardState makeMove(short move) {
    BoardState tempBoard = new BoardState(this);
    return Mover.makeMove(tempBoard, move);
  }

  private void incrementPiece(byte piece) {
    if (piece == 0) return;
    pieceMap.merge(piece, 1, Integer::sum);
  }

  public void updatePieceCount() {
    pieceMap.clear();
    for (byte piece : board.getBoard()) incrementPiece(piece);
  }

  public String toFenString() {
    return FenUtils.getFenString(this);
  }

  public void printBoard(BoardState board) {
    System.out.println(FenUtils.getFenString(board));
    System.out.println(board.board.toString());
  }

  public void printBoard() {
    printBoard(this);
  }
}
