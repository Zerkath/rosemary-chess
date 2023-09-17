package com.github.zerkath.rosemary.BoardRepresentation;

import com.github.zerkath.rosemary.DataTypes.*;
import java.util.HashMap;
import java.util.Map;

public class BoardState {

  public Board board = new Board();
  public BoardState previous;
  public boolean isWhiteTurn;

  public int turnNumber = 1;
  public int halfMove = 0;

  public Map<Integer, Integer> pieceMap = new HashMap<>();

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
    this.previous = state.previous;
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
        int piece = Pieces.getNum(ch);
        board.replaceCoordinate(Utils.getCoordinate(row, column), piece);
        incrementPiece(piece);
        column++;
      }
    }
  }

  public void playMoves(String[] moves) {
    for (String move : moves) {
      short x = MoveUtil.getMove(move);
      System.out.println(MoveUtil.moveToString(x));
      this.makeMove(x);
    }
  }

  private void addEnPassantMove(boolean white, int piece, int dCol, int dRow) {
    boolean pieceIsWhite = Pieces.isWhite(piece);
    if (white && !pieceIsWhite) {
      enPassant = Utils.getCoordinate(dRow + 1, dCol);
    }
    if (!white && pieceIsWhite) {
      enPassant = Utils.getCoordinate(dRow - 1, dCol);
    }
  }

  private void checkForCastlingRights(short move) {
    short destination = MoveUtil.getDestination(move);
    short origin = MoveUtil.getOrigin(move);

    short oRow = MoveUtil.getRow(origin);
    short oCol = MoveUtil.getColumn(origin);
    short dRow = MoveUtil.getRow(destination);
    short dCol = MoveUtil.getColumn(destination);

    CastlingRights blackCastling = board.getBlackCastling();
    CastlingRights whiteCastling = board.getWhiteCastling();

    if (oRow == 0 && oCol == 0 || dRow == 0 && dCol == 0) { // black queen side rook
      if (blackCastling == CastlingRights.BOTH) {
        board.setBlackCastlingRights(CastlingRights.KING);
      } else if (blackCastling == CastlingRights.QUEEN) {
        board.setBlackCastlingRights(CastlingRights.NONE);
      }
    } else if (oRow == 0 && oCol == 7 || dRow == 0 && dCol == 7) { // black king side rook
      if (blackCastling == CastlingRights.BOTH) {
        board.setBlackCastlingRights(CastlingRights.QUEEN);
      } else if (blackCastling == CastlingRights.KING) {
        board.setBlackCastlingRights(CastlingRights.NONE);
      }
    } else if (oRow == 7 && oCol == 0 || dRow == 7 && dCol == 0) { // white queen side rook
      if (whiteCastling == CastlingRights.BOTH) {
        board.setWhiteCastlingRights(CastlingRights.KING);
      } else if (whiteCastling == CastlingRights.QUEEN) {
        board.setWhiteCastlingRights(CastlingRights.NONE);
      }
    } else if (oRow == 7 && oCol == 7 || dRow == 7 && dCol == 7) { // white king side rook
      if (whiteCastling == CastlingRights.BOTH) {
        board.setWhiteCastlingRights(CastlingRights.QUEEN);
      } else if (whiteCastling == CastlingRights.KING) {
        board.setWhiteCastlingRights(CastlingRights.NONE);
      }
    }
  }

  public void unMakeMove() {
    setBoardState(previous);
  }

  /** Returs a copy of the board with the new move */
  public BoardState makeNonModifyingMove(short move) {
    BoardState tempBoard = new BoardState(this);
    tempBoard.makeMove(move);
    return tempBoard;
  }

  public void makeMove(short moveWithPromotionData) {
    previous = new BoardState(this);
    short move = MoveUtil.clearPromotion(moveWithPromotionData);
    short temp_destination = MoveUtil.getDestination(move);
    short temp_origin = MoveUtil.getOrigin(move);
    int dRow = MoveUtil.getRow(temp_destination);
    int dCol = MoveUtil.getColumn(temp_destination);

    int selected = board.getCoordinate(temp_origin);

    boolean isBeingPromoted = MoveUtil.getPromotion(moveWithPromotionData) != 0;
    boolean isWhite = Pieces.isWhite(selected);

    checkForCastlingRights(moveWithPromotionData);

    if (Pieces.getType(selected) == Pieces.PAWN
        || board.getCoordinate(MoveUtil.getDestination(move)) != 0) {
      halfMove = 0;
    } else {
      halfMove++;
    }

    if (Pieces.getType(selected) == Pieces.PAWN
        && enPassant != -1
        && MoveUtil.getRow(enPassant) == MoveUtil.getRow(temp_destination)
        && MoveUtil.getColumn(enPassant) == MoveUtil.getColumn(temp_destination)) {
      int offSet = isWhite ? 1 : -1;
      board.clearCoordinate(MoveUtil.getRow(enPassant) + offSet, MoveUtil.getColumn(enPassant));
    }

    enPassant = -1;
    // add En passant
    if (Pieces.getType(selected) == Pieces.PAWN
        && ((MoveUtil.getRow(temp_origin) == 6 && MoveUtil.getRow(temp_destination) == 4)
            || (MoveUtil.getRow(temp_origin) == 1 && MoveUtil.getRow(temp_destination) == 3))) {

      int right = 0;
      int left = 0;
      if (dCol == 0) right = board.getCoordinate(dRow, dCol + 1);
      if (dCol == 7) left = board.getCoordinate(dRow, dCol - 1);
      if (dCol > 0 && dCol < 7) {
        right = board.getCoordinate(dRow, dCol + 1);
        left = board.getCoordinate(dRow, dCol - 1);
      }

      if (Pieces.getType(right) == Pieces.PAWN) {
        addEnPassantMove(isWhite, right, dCol, dRow);
      }

      if (Pieces.getType(left) == Pieces.PAWN) {
        addEnPassantMove(isWhite, left, dCol, dRow);
      }
    }

    // Castling
    if (Pieces.getType(selected) == Pieces.KING
        && MoveUtil.getColumn(temp_origin) == 4
        && ((MoveUtil.getRow(temp_origin) == 0 && dRow == 0)
            || MoveUtil.getRow(temp_origin) == 7 && dRow == 7)
        && (dCol == 2 || dCol == 6)) {

      board.setCastling(CastlingRights.NONE, isWhite);

      int rook;
      short destination;
      short origin;
      if (dCol == 2) {
        destination = Utils.getCoordinate(dRow, 3);
        origin = Utils.getCoordinate(dRow, 0);
      } else {
        destination = Utils.getCoordinate(dRow, 5);
        origin = Utils.getCoordinate(dRow, 7);
      }
      rook = board.getCoordinate(origin);
      board.replaceCoordinate(destination, rook);
      board.clearCoordinate(origin);
    }

    if (Pieces.getType(selected) == Pieces.KING) {
      board.setCastling(CastlingRights.NONE, isWhite);
    }

    board.clearCoordinate(temp_origin);

    int piece = selected;
    if (isBeingPromoted) {
      piece = Pieces.getPromotionNum(MoveUtil.getPromotion(moveWithPromotionData));
    }

    board.replaceCoordinate(temp_destination, piece);

    if (!this.isWhiteTurn) turnNumber++;

    this.isWhiteTurn = !this.isWhiteTurn;
  }

  private void incrementPiece(int piece) {
    if (piece == 0) return;
    pieceMap.merge(piece, 1, Integer::sum);
  }

  public void updatePieceCount() {
    pieceMap.clear();
    for (int piece : board.getBoard()) incrementPiece(piece);
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
