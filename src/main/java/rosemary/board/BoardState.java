package rosemary.board;

import java.util.Arrays;
import rosemary.types.*;

public class BoardState {

    // public Board board = new Board();
    public boolean isWhiteTurn;

    public short turnNumber = 1;
    public short halfMove = 0;
    public byte[] board = new byte[64];
    public CastlingRights whiteCastlingRights = CastlingRights.NONE;
    public CastlingRights blackCastlingRights = CastlingRights.NONE;
    public short whiteKing = -1;
    public short blackKing = -1;

    public short enPassant = -1;

    public BoardState() {
        Arrays.fill(board, (byte) 0);
    }

    public BoardState(BoardState state) {
        setBoardState(state);
    }

    public BoardState(String fen) {
        setBoardState(FenUtils.parseFen(fen));
    }

    public void setBoardState(BoardState state) {
        this.board = BoardUtils.copy(state.board);
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
                    board[Utils.getCoordinate(row, column)] = 0; // clearing the coordinate
                    column++;
                }
            } else {
                byte piece = Pieces.getNum(ch);
                replaceCoordinate(Utils.getCoordinate(row, column), piece);
                column++;
            }
        }
    }

    /**
     * returns a piece map of the board state index is the piece number, value is the number of
     * pieces on the board there are some empty spaces in the array, but maybe cache locality is
     * worth it?
     */
    public byte[] getPieceMap() {
        byte[] pieceMap = new byte[23]; // 22 is the max value of king + white
        Arrays.fill(pieceMap, (byte) 0); // ensure values are initialised

        // unrolled loop for performance
        for (int i = 0; i < 64; i += 8) {
            byte piece1 = board[i];
            byte piece2 = board[i + 1];
            byte piece3 = board[i + 2];
            byte piece4 = board[i + 3];
            byte piece5 = board[i + 4];
            byte piece6 = board[i + 5];
            byte piece7 = board[i + 6];
            byte piece8 = board[i + 7];

            if (piece1 != 0) pieceMap[piece1] = (byte) (1 + pieceMap[piece1]);
            if (piece2 != 0) pieceMap[piece2] = (byte) (1 + pieceMap[piece2]);
            if (piece3 != 0) pieceMap[piece3] = (byte) (1 + pieceMap[piece3]);
            if (piece4 != 0) pieceMap[piece4] = (byte) (1 + pieceMap[piece4]);
            if (piece5 != 0) pieceMap[piece5] = (byte) (1 + pieceMap[piece5]);
            if (piece6 != 0) pieceMap[piece6] = (byte) (1 + pieceMap[piece6]);
            if (piece7 != 0) pieceMap[piece7] = (byte) (1 + pieceMap[piece7]);
            if (piece8 != 0) pieceMap[piece8] = (byte) (1 + pieceMap[piece8]);
        }

        return pieceMap;
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
        board[coordinate] = piece;
    }
}
