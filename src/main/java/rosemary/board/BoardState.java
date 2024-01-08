package rosemary.board;

import java.util.HashMap;
import java.util.Map;
import rosemary.types.*;

public class BoardState {

  public Board board = new Board();
  public boolean isWhiteTurn;

  public int turnNumber = 1;
  public int halfMove = 0;
  private CastlingRights whiteCastlingRights = CastlingRights.NONE;
  private CastlingRights blackCastlingRights = CastlingRights.NONE;
  private short whiteKing = -1;
  private short blackKing = -1;

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
    this.whiteCastlingRights = state.whiteCastlingRights;
    this.blackCastlingRights = state.blackCastlingRights;
    this.whiteKing = state.whiteKing;
    this.blackKing = state.blackKing;
  }

  public void setCastling(char[] castling) {
    if (castling.length == 1 && castling[0] == '-') {
      setWhiteCastlingRights(CastlingRights.NONE);
      setBlackCastlingRights(CastlingRights.NONE);
      return;
    }

    for (char c : castling) {
      boolean white = !Character.isLowerCase(c);
      boolean queen = Character.toLowerCase(c) == 'q';
      CastlingRights curr = white ? getWhiteCastling() : getBlackCastling();
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

      setCastling(curr, white);
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
        replaceCoordinate(Utils.getCoordinate(row, column), piece);
        incrementPiece(piece);
        column++;
      }
    }
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

  public CastlingRights getWhiteCastling() {
    return whiteCastlingRights;
  }

  public CastlingRights getBlackCastling() {
    return blackCastlingRights;
  }

  public void setWhiteCastlingRights(CastlingRights rights) {
    this.whiteCastlingRights = rights;
  }

  public void setBlackCastlingRights(CastlingRights rights) {
    this.blackCastlingRights = rights;
  }

  public void setCastling(CastlingRights rights, boolean white) {
    if (white) setWhiteCastlingRights(rights);
    else setBlackCastlingRights(rights);
  }

  private void replaceKing(short coordinate, int piece) {
    if (Pieces.isWhite(piece)) setWhiteKing(coordinate);
    else setBlackKing(coordinate);
  }

  public void replaceCoordinate(short coordinate, byte piece) {
    if (piece != 0 && Pieces.getType(piece) == Pieces.KING) replaceKing(coordinate, piece);
    board.replaceCoordinate(MoveUtil.getRow(coordinate), MoveUtil.getColumn(coordinate), piece);
  }
}
